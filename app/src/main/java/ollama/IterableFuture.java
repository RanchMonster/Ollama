package ollama;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
            private int index = 0;

            @Override
            public boolean hasNext() {
                if (index < futures.size()) {
                    return true;
                } else if (!closed) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return hasNext();
                } else {
                    return false;
                }
            }

            @Override
            public T next() {
                if (hasNext()) {
                    try {
                        T future = futures.get(index).await();
                        index++;
                        return future;
                    } catch (CoroutineError e) {
                        e.printStackTrace();
                        throw new NoSuchElementException();
                    }
                } else {
                    throw new NoSuchElementException();
                }
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
