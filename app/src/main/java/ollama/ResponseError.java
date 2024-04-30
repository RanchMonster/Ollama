package ollama;
public class ResponseError extends Throwable {
    private String context;
    private int responseCode;
    public  ResponseError(String context,int responseCode){
        this.context=context;
        this.responseCode=responseCode;

    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "context='" + context + '\'' +
                ", responseCode=" + responseCode +
                '}';
    }
}
