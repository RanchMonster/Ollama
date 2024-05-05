package ollama;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.JSONObject;
import org.json.JSONException;
class OllamaMessageBuffer implements Iterable<OllamaMessage> {
    private BufferedReader reader;
    private OllamaMessage lastMessage;
    OllamaMessageBuffer(BufferedReader reader) {
        this.reader = reader;
    }
    public Iterator<OllamaMessage> iterator() {
        return new Iterator<OllamaMessage>() {
            private String line;
            private OllamaMessage message;
            public boolean hasNext() {
                try {
                    line = reader.readLine();
                    if (line == null) {
                        return false;
                    }
                    JSONObject json= new JSONObject(line);
                    OllamaMessage lastMessage=new OllamaMessage(json.getString("content"), json.getString("role"), json.getBoolean("done"));
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            public OllamaMessage next() {
                return lastMessage;
            }
        };
    }
    public OllamaMessage nostream(){
        String curr;
        JSONObject obj;
        try {
            while ((curr = reader.readLine())!=null) {
                try{
                    obj = new JSONObject(curr);
                }catch( e){
                    return null;
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
}

