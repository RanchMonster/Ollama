package ollama;

public class CoroutineError extends Throwable{
    private final String context;
    public  CoroutineError(String context){

        this.context = context;
    }

    @Override
    public String toString() {
        return "CoroutineError{" +
                "context='" + context + '\'' +
                '}';
    }
}
