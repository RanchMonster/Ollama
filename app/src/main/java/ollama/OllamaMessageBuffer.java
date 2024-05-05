package ollama;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.JSONObject;
import org.json.JSONException;
class OllamaMessageBuffer implements Iterable<OllamaMessage> {
    private Iterator<String> reader;
    OllamaMessageBuffer(Iterator<String> reader) {
        this.reader = reader;
        while (!reader.hasNext()) {
            continue;
        }
    }
    public Iterator<OllamaMessage> iterator() {
        return new Iterator<OllamaMessage>() {
            public boolean hasNext() {
                return reader.hasNext();
            }
            public OllamaMessage next() {
                JSONObject obj = new JSONObject(reader.next());
                OllamaMessage message=new OllamaMessage(obj.getString("content"), obj.getString("role"), obj.getBoolean("done"));
                return message;
            }
        };
    }
    public OllamaMessage nostream() throws IOException{
        String curr;
        JSONObject obj;
        OllamaMessageList messages=new OllamaMessageList();
            while ((curr = reader.next())!=null) {
                try{
                    obj = new JSONObject(curr);
                    messages.addMessage(new OllamaMessage(obj.getString("content"), obj.getString("role"), obj.getBoolean("done")));
                }catch(JSONException e){
                    continue;
                }
            }
            OllamaMessage Message=messages.getMessage(0);
            for (int x = 1; x < messages.size(); x++) {
                Message.mergeChunck(messages.getMessage(x));
            }
            return Message;
    }
}

