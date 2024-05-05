package ollama;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.json.JSONObject;
import org.json.JSONException;
class OllamaMessageBuffer extends BufferedReader{
    private OllamaMessageList messages;
    public OllamaMessageBuffer(Reader in) {
        super(in);
    }
    public OllamaMessageBuffer(Reader in, int sz) {
        super(in, sz);
    }
    
}

