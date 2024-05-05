package ollama;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.json.JSONObject;
import org.json.JSONException;
class OllamaMessageBuffer extends BufferedReader implements Iterable<OllamaMessage>{
    private OllamaMessageList messages;
    public OllamaMessageBuffer(Reader in) {
        super(in);
    }
    public OllamaMessageBuffer(Reader in, int sz) {
        super(in, sz);
        try {
            generate();
        } catch (JSONException | IOException e) {
            System.err.println("unreadable response");
        }
    }
    private boolean generate() throws IOException, JSONException {
        String line;
        while ((line= super.readLine())!=null) {
            JSONObject json=new JSONObject();
            OllamaMessage message=new OllamaMessage(json.getString("content"),json.getString("role"),json.getBoolean("done"));
            messages.addMessage(message);
        }
        return true;
    }
    public Iterator<OllamaMessage> iterator(){
        return messages.iterator();
    }
}

