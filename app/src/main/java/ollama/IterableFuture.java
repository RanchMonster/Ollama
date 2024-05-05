package ollama;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class IterableFuture<T> implements Iterable<T> {
    private ArrayList<BetterFuture<T>> futures;
    /**
     * Iterable Future is used for Futures that you want to 
     */
    public IterableFuture(){
        this.futures= new ArrayList<BetterFuture<T>>();
    }
    /**
     * 
     * @param value a betterFuture Object to add to the Iterable Future
     */
    public void Future(BetterFuture<T> value){
        futures.add(value);
    }
    public Iterator<T> iterator(){
        return new Iterator<T>() {
            private int currentIndex=0;
            private T future;
            @Override
            public boolean hasNext() {
               if(currentIndex<futures.size()){
                    try {
                        future = futures.get(currentIndex).await();
                    } catch (CoroutineError e) {
                        return false;
                    }
                    return true;
               }else return false;
            }
            public T next(){
                return future;
            }
        };
    }
    /**
     * @return returns the first Future and removes it from the iterable future
     */
    public T await() throws CoroutineError{
        return futures.remove(0).await();
    }
}
