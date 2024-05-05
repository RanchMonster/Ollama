package ollama;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.json.JSONObject;
import org.json.JSONException;
class OllamaMessageBuffer extends BufferedReader implements Iterable<OllamaMessage> {
    public OllamaMessageBuffer(Reader in) {
        super(in);
    }
    public OllamaMessageBuffer(Reader in, int sz) {
        super(in, sz);
    }
    public Iterator <OllamaMessage> iterator() {
        return new Iterator<OllamaMessage>() {
            @Override

            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }
        };
}

