package ocpjp.pretest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class SystemError {

    public static void main(String[] s) throws FileNotFoundException {

        OutputStream os = new FileOutputStream("log.txt");
        System.setErr(new PrintStream(os)); // SET SYSTEM.ERR
        System.err.println("Error");
    }
}