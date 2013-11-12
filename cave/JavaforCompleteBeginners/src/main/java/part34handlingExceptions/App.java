package part34handlingExceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 Exceptions are a major part of the Java language; so much so that you can’t write 
 * very much code without at least having to handle some of them. In this tutorial 
 * we’ll get started with Exceptions, and I’ll also give you some tips for taking 
 * the string out of those big red error messages that so horrify beginners.
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("test.txt");
        FileReader fr = new FileReader(file);
    }
    
}
