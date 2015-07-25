import java.io.*;
import java.util.*;

public class Type {
   public static void main(String[] args) {
      String file = args[0];
      try {
         BufferedReader inFile = new BufferedReader(new FileReader(file));
         System.out.println("File found. Output to come later.");
      }
      catch (FileNotFoundException e) {
         System.out.print("File not found. Try again.");
      }   
   }
}