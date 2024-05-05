package ollama;

import org.json.JSONObject;

public class OllamaMessage {
    private String content;
    private String role;
    private Boolean done;
    public static final String USER = "user";
    public static final String ASSISTANT = "assistant";
    public static final String SYSTEM  = "system";
    public OllamaMessage(String content, String role, Boolean done) {
        this.content = content;
        this.role = role;
        this.done = done;
    
    }
    /**
     * this is for user Meassges 
     */
    public OllamaMessage(String content, String role){
        this(content, role, false);
    }
    public String getContent() {
        return this.content;
    }
    public String getRole() {
        return this.role;
    }
    public String toString(){
        return "OllamaMessage{role:" +role + ", content:" + content + ", done:" + done + "}";
    }
    public JSONObject toJsonObject(){
        JSONObject data=new JSONObject();
        data.put("content",content);
        data.put("role", role);
        return data;
    }
    public void mergeChunck(OllamaMessage other){
        this.content+=other.content;
        
    }
}
