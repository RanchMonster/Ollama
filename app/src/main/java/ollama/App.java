/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ollama;


public class App {
        public static void main(String[] args) throws ResponseError {
        try {
            Ollama AI =Ollama.loadDefaultHost();
            System.out.println(AI.chat("", null, false, null, null, null));
        } catch (RequestError | CoroutineError | Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
    }

