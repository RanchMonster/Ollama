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
    public OllamaMessage nostream() throws IOException{
        String curr;
        JSONObject obj;
        curr = reader.readLine();
        OllamaMessageList messages=new OllamaMessageList();
        try {
            while ((curr = reader.readLine())!=null) {
                try{
                    obj = new JSONObject(curr);
                    messages.addMessage(new OllamaMessage(obj.getString("content"), obj.getString("role"), obj.getBoolean("done")));
                }catch(JSONException e){
                    continue;
                }
            }
            OllamaMessage lastMessage=messages.getMessage(0);
            for (int x = 1; x < messages.size(); x++) {
                lastMessage.mergeChunck(messages.getMessage(x));
            }
            return lastMessage;
        } catch (IOException e) {
            return null;
        }
    }
}

