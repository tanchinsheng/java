package part35throwingexceptions;

public class Test2 {
    public void run() throws ServerException{
        //  Some kind of return value from some complex process!
        // 0 = success
        // anything else = error code
        int code = 1;
        if (code!= 0) {
            // Something's wrong!
            throw new ServerException("Could not connect to server.");
        }
        
        System.out.println("Running successfully");
    }
    
}
