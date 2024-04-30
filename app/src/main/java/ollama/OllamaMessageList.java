package ollama;
import org.json.JSONArray;
import org.json.JSONObject;

public class OllamaMessageList {
    private JSONArray messages;
    public OllamaMessageList(){
        messages=new JSONArray();
    }
    public void addMessage(OllamaMessage message){
        messages.put(message.toJsonObject());
    }
    public JSONArray toJsonArray(){
        return messages;
    }
    public String toString(){
        return messages.toString();
    }
    public void clear(){
        messages=new JSONArray();
    }
    public int size(){
        return messages.length();
    }
    public OllamaMessage getMessage(int index){
        JSONObject message=messages.getJSONObject(index);
        return new OllamaMessage(message.getString("content"),message.getString("role"),message.getBoolean("done"));
    }


}