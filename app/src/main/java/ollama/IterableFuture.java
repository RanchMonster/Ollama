package ollama;
import java.util.ArrayList;
import java.util.Iterator;
public class IterableFuture<T> implements Iterable<T> {
    private ArrayList<BetterFuture<T>> futures;
    private boolean closed;
    /**
     * Iterable Future is used for when you have many Better Futures you want to go through 
     * similar to A Async for loop you find in other languages
     */
    public IterableFuture(){
        this.futures= new ArrayList<BetterFuture<T>>();
        this.closed=false;
    }
    /**
     * 
     * @param value a betterFuture Object to add to the Iterable Future
     * @throws CoroutineError 
     */
    public void Future(BetterFuture<T> value) throws CoroutineError{
        if(closed)throw new CoroutineError("you can not add a Future to a closed Future");
        futures.add(value);
    }
    public Iterator<T> iterator(){
        
        return new Iterator<T>() {
            private T future;
            @Override
            public boolean hasNext() {
                while (!closed &&futures.isEmpty()) {
                }
                if(!futures.isEmpty()){
                    try{
                        future=futures.remove(0).await();
                    }catch(CoroutineError e){
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }else{
                    return false;
                }
            }
            public T next(){
                return future;
            }
        };
    }
    /**
     * @return returns the first Future and removes it from the iterable future
     * @throws CoroutineError
     */
    public T await() throws CoroutineError{
        while (!closed) {
            try{
                return futures.remove(0).await();
            }catch(IndexOutOfBoundsException e){
                continue;
            }
        }
        return null;

    }
    
    public void close(){
        this.closed=true;
    }
}
