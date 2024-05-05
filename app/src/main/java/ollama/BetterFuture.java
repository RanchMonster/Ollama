package ollama;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class BetterFuture<T> extends CompletableFuture<T> {
    /**
     * allows you to await a return with out Interrupting it
     * @return the Completed Futures return value
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public T await() throws CoroutineError {
        try {
            while (!this.isDone()) {

            }
            return this.get();
        } catch (ExecutionException e) {
            throw new CoroutineError("The Following error occurred "+e);
        } catch (InterruptedException e) {
            throw new CoroutineError("was never awaited or was interrupted by another process ");
        }
    }

    public static <U> BetterFuture<U> runBetterAsync(Supplier<U> supplier, Executor executor) {
        BetterFuture<U> future = new BetterFuture<>();
        executor.execute(() -> {
            try {
                future.complete(supplier.get());
            } catch (Throwable ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }
}
