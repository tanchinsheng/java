package part40trywithresources;

/**
Java 7 introduces a great new language feature, “try with resources”. 
* In this tutorial we’ll see how we can use a try block to simply our file reading code 
* quite a lot, ensuring that file handles are closed without the need of a hideous 
* nested try block or a finally clause. We’ll also look at AutoCloseable.
 */
class Temp implements AutoCloseable {
    @Override
    public void close() throws Exception {
        System.out.println("Closing!");
        throw new Exception("oh no!");
    }  
}

public class App { 
    public static void main(String[] args) {     
        try (Temp temp = new Temp()) {          
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }
}