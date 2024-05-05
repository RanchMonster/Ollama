package ollama;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.json.JSONObject;
import org.json.JSONException;
class OllamaMessageBuffer extends BufferedReader{
    private OllamaMessageBuffer(Reader in) {
        super(in);
    }
    private OllamaMessageBuffer(Reader in, int sz) {
        super(in, sz);
    }
    public static IterableFuture<OllamaMessage> generate(Reader in) throws IOException, JSONException {
        IterableFuture<OllamaMessage> messages = new IterableFuture<OllamaMessage>();
        new Thread(()->{
            BetterFuture<OllamaMessage> current=new BetterFuture<OllamaMessage>();
            try {
                messages.Future(current);
            } catch (CoroutineError e) {
                e.printStackTrace();
            }
            OllamaMessageBuffer buffer = new OllamaMessageBuffer(in);
            String line;
            try {
                while ((line= buffer.readLine())!=null) {
                    JSONObject json=new JSONObject();
                    OllamaMessage message=new OllamaMessage(json.getString("content"),json.getString("role"),json.getBoolean("done"));
                    current.complete(message);
                }
            } catch (JSONException | IOException e) {
                current.completeExceptionally(e);
            }
        }).start();
        return messages;
    }
}

