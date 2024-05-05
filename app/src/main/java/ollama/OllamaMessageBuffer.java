package ollama;
public class OllamaMessageBuffer extends OllamaMessage {
    private String buffer;
    public OllamaMessageBuffer(String content, String role){
        super(content, role);
    }
}
