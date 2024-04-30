package ollama;
public class RequestError extends Throwable{
    private String context;
    public RequestError(String context){
        this.context=context;
    }

    @Override
    public String toString() {
        return "RequestError{" +
                "context='" + context + '\'' +
                '}';
    }
}
