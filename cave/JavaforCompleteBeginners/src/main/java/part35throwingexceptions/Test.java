package part35throwingexceptions;

import java.io.IOException;

public class Test {
    public void run() throws IOException{
        //  Some kind of return value from some complex process!
        // 0 = success
        // anything else = error code
        int code = 1;
        if (code!= 0) {
            // Something's wrong!
            throw new IOException("Could not connect to server.");
        }
        
        System.out.println("Running successfully");
    }
    
}
