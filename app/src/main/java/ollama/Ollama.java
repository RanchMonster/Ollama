package ollama;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.*;
public class Ollama {
    private final String host;

    /**
     * 
     * @param host the host you want to use
     * @throws IOException
     * @throws RequestError
     * @throws CoroutineError
     * @throws ResponseError
     */
    public Ollama(String host) throws IOException, RequestError, CoroutineError, ResponseError {
        this.host = host;
        int code= checkOllama().await();
        if(code!=200){
            throw new ResponseError("Host gave invaild status code",code);
        }
    }
    /**
     * 
     * @return the current default host is https://dnjrepair.com:7007
     * @throws IOException
     * @throws RequestError
     * @throws CoroutineError
     * @throws ResponseError
     */
    public static Ollama loadDefaultHost() throws IOException, RequestError, CoroutineError, ResponseError {
        return new Ollama("https://dnjrepair.com:7007");
    }
    /**
     * 
     * @return the response code of the host
     * @throws IOException
     * @throws CoroutineError
     * @throws RequestError
     */
    public BetterFuture<Integer> checkOllama() throws IOException, CoroutineError, RequestError {
        BetterFuture<Integer> future = new BetterFuture<>();
        Thread thread = new Thread(() -> {
            try {
                HttpsURLConnection connection;
                try {
                    connection = request("GET", "", null).await();
                } catch (CoroutineError | RequestError e) {
                    future.completeExceptionally(e);
                    connection=null;
                    e.printStackTrace();
                }
                future.complete(connection.getResponseCode());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        thread.start();
        return future;
    }
    private BetterFuture<HttpsURLConnection> request(String method, String url, JSONObject json) throws RequestError {
        BetterFuture<HttpsURLConnection> future = new BetterFuture<HttpsURLConnection>();
        new Thread(() -> {
            try {
                HttpsURLConnection client = createConnection(method, url, json);
                future.complete(client);
            } catch (IOException | RequestError e) {
                future.completeExceptionally(e);
            }
        }).start();

        return future;
    }

    private HttpsURLConnection createConnection(String method, String url, JSONObject json) throws IOException, RequestError {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllCertificates() };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection client = (HttpsURLConnection) new URI(host + url).toURL().openConnection();
            client.setRequestMethod(method);
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");
            client.setDoOutput(true);

            if (json != null) {
                try (OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream())) {
                    writer.write(json.toString());
                    writer.flush();
                }
            }

            return client;
        } catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new RequestError(host + " is an invalid host");
        }
    }


    private JSONObject requestToJson(HttpsURLConnection connection) throws IOException, JSONException, ResponseError {
        int responseCode = connection.getResponseCode();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            return new JSONObject(responseBuilder.toString());
        } catch (JSONException e) {
            throw new ResponseError("Error parsing response JSON", responseCode);
        }
    }

    private JSONObject stream(String method, String url, JSONObject json) throws IOException, ResponseError, RequestError, CoroutineError {
        HttpsURLConnection connection = request(method, url, json).await();


        int responseCode = connection.getResponseCode();
        if (responseCode >= 400) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String errorMessage = reader.lines().collect(Collectors.joining("\n"));
                throw new ResponseError(errorMessage, responseCode);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            JSONObject responseJson = new JSONObject();
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    JSONObject partial = new JSONObject(line);
                    if (partial.has("error")) {
                        throw new ResponseError(partial.getString("error"), responseCode);
                    }else{
                        responseJson= partial;
                    }
                }catch (Exception e){
                    throw new ResponseError("did not responed with json or could not load the json string",500);
                }

                // If you want to yield a JSON object for each line, you can yield the partial JSON object here
            }
            return responseJson;
        }
    }

    public JSONObject requestStream(String method, String url, JSONObject json, boolean stream) throws IOException, ResponseError, RequestError, CoroutineError {
        return stream(method, url, json);
    }

    /**
     * Generate a text from a prompt
     * @param model The model to use
     * @param prompt The prompt to use
     * @param system The system prompt to use
     * @param template A text template to use
     * @param context The context to use
     * @param stream Stream the response
     * @param raw 
     * @param format
     * @param images
     * @param options
     * @param keepAlive
     * @return
     * @throws Exception
     * @throws ResponseError
     * @throws RequestError
     * @throws CoroutineError
     */
    public String generate(String model, String prompt, String system, String template,
                               List<Integer> context, boolean stream, boolean raw, String format,
                               List<String> images, JSONObject options, String keepAlive)
            throws Exception, ResponseError, RequestError, CoroutineError {
        if (model == null || model.isEmpty()) {
            throw new RequestError("must provide a model");
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("prompt", prompt);
        requestBody.put("system", system);
        requestBody.put("template", template);
        requestBody.put("context", context != null ? context : Collections.emptyList());
        requestBody.put("stream", stream);
        requestBody.put("raw", raw);
        requestBody.put("format", format);
        requestBody.put("images", images != null ? images.stream().map(this::encodeImage).collect(Collectors.toList()) : Collections.emptyList());
        requestBody.put("options", options != null ? options : new JSONObject());
        requestBody.put("keep_alive", keepAlive);

        JSONObject data=requestStream("POST", "/api/generate", requestBody, stream);
        return data.getString("response");
    }
    /**
     * 
     * @param model
     * @param messageList
     * @param stream
     * @param format
     * @param options
     * @param keepAlive
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws RequestError
     * @throws ResponseError
     * @throws CoroutineError
     */
    public OllamaMessage chat(String model, OllamaMessageList messageList, boolean stream, String format,
                           JSONObject options, String keepAlive) throws IOException, JSONException, RequestError, ResponseError, CoroutineError {
        if (model == null || model.isEmpty()) {
            throw new RequestError("must provide a model");
        }
        JSONArray messages = new JSONArray();
        for(OllamaMessage x:messageList){
            messages.put(x.toJsonObject());
        }

        if (messages != null) {
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String role = message.optString("role");
                if (role == null || !Arrays.asList("system", "user", "assistant").contains(role)) {
                    throw new RequestError("messages must contain a role and it must be one of 'system', 'user', or 'assistant'");
                }
                if (!message.has("content")) {
                    throw new RequestError("messages must contain content");
                }
                if (message.has("images")) {
                    JSONArray messageImages = message.getJSONArray("images");
                    for (int j = 0; j < messageImages.length(); j++) {
                        String image = messageImages.getString(j);
                        messageImages.put(j, encodeImage(image));
                    }
                }
            }
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages != null ? messages : new JSONArray());
        requestBody.put("stream", stream);
        requestBody.put("format", format);
        requestBody.put("options", options != null ? options : new JSONObject());
        requestBody.put("keep_alive", keepAlive);

        JSONObject data=requestStream("POST", "/api/chat", requestBody, stream);
        JSONObject response = data.getJSONObject("message");
        OllamaMessage message = new OllamaMessage(response.getString("content"), response.getString("role"),data.getBoolean("done"));
        return message;
    }

    public List<Double> embeddings(String model, String prompt, JSONObject options, String keepAlive) throws IOException, JSONException, ResponseError, RequestError, CoroutineError {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("prompt", prompt);
        requestBody.put("options", options != null ? options : new JSONObject());
        requestBody.put("keep_alive", keepAlive);

        JSONObject response =requestToJson(request("POST", "/api/embeddings", requestBody).await());
        if (!response.has("embeddings")) {
            throw new ResponseError("Response missing embeddings data", response.getInt("status"));
        }

        JSONArray embeddingsArray = response.getJSONArray("embeddings");
        List<Double> embeddings = new ArrayList<>();
        for (int i = 0; i < embeddingsArray.length(); i++) {
            embeddings.add(embeddingsArray.getDouble(i));
        }
        return embeddings;
    }

    public JSONObject pull(String model, boolean insecure, boolean stream) throws IOException, JSONException, ResponseError, RequestError, CoroutineError {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", model);
        requestBody.put("insecure", insecure);
        requestBody.put("stream", stream);

        return requestStream("POST", "/api/pull", requestBody, stream);
    }



//    public JSONObject delete(String model) throws IOException {
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("name", model);
//
//        JSONObject response = request("DELETE", "/api/delete", requestBody);
//        JSONObject result = new JSONObject();
//        result.put("status", response.getInt("status") == 200 ? "success" : "error");
//        return result;
//    }

    public ArrayList<String> list() throws IOException, JSONException, ResponseError, RequestError, CoroutineError {
        JSONObject data=requestToJson(request("GET", "/api/tags", null).await());
        JSONArray list=data.getJSONArray("models");
        ArrayList<String> outputList=new ArrayList<String>();
        for(int i=0;i<list.length();i++){
            outputList.add(list.getJSONObject(i).getString("model"));
        }
        return outputList;
    
    }

    // Helper method for encoding images
    private String encodeImage(String image) {
        // Implementation for encoding images
        return image;
    }
    }
