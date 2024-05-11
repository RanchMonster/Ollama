package ollama;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class OllamaMessageList implements Iterable<OllamaMessage>,Serializable {
    private ArrayList<OllamaMessage> messages;
    public OllamaMessageList(){
        messages= new ArrayList<OllamaMessage>();
    }
    public void addMessage(OllamaMessage message){
        messages.add(message);
    }
    public String toString(){
        return messages.toString();
    }
    public void clear(){
        messages= new ArrayList<OllamaMessage>();
    }
    public int size(){
        return messages.size();
    }
    public OllamaMessage getMessage(int index){
        OllamaMessage message=messages.get(index);
        return message;
    }
    public Iterator<OllamaMessage> iterator(){
        int maxLength=this.size();
        return new Iterator<OllamaMessage>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < maxLength;
            }

            @Override
            public OllamaMessage next() {
                return messages.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
