package part34handlingExceptions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class App2 {
    public static void main(String[] args) {
        File file = new File("test.txt");
        try {
            FileReader fr = new FileReader(file);
            //Not run if exception is thrown.
            System.out.println("Continuing...");
        } catch (FileNotFoundException ex) {
            System.out.println("No file!");
        }
    }
    
}