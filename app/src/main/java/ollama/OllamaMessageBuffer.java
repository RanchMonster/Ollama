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
        try {
            generate();
        } catch (JSONException | IOException e) {
            System.err.println("unreadable response");
        }
    }
    public OllamaMessageBuffer(Reader in, int sz) {
        super(in, sz);
        try {
            generate();
        } catch (JSONException | IOException e) {
            System.err.println("unreadable response");
        }
    }
    private void generate() throws IOException, JSONException {
        JSONObject json=new JSONObject(super.readLine());
        OllamaMessage message=new OllamaMessage(json.getString("content"),json.getString("role"),json.getBoolean("done"));
        messages.addMessage(message);
    }
    public Iterator<OllamaMessage iterator(){
        return messages.iterator();
    }
}

