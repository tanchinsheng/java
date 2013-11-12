/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */

package part41writingtextfiles;

/**
A tutorial on writing text files, Java 7 style. 
* If you want Java 6 style, you can easily take the code here and re-arrange 
* it along the lines of the previous tutorial on reading text files in Java.
* Writing text files requires the same kind of Russian doll approach that
* reading them does, unfortunately, but at least you can simplify the code a 
* lot using the try-with-resources syntax that Java 7 introduced.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
 
 
public class App {
 
     
    public static void main(String[] args) {
        File file = new File("testwrite.txt");
          
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
           br.write("This is line one");
           br.newLine();
           br.write("This is line two\n");
           br.write("Last line.");
        } catch (IOException e) {
            System.out.println("Unable to read file " + file.toString());
        }
  
 
    }
 
}