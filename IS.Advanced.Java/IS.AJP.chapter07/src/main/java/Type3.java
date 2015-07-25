
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Type3 {

    public static void main(String[] args) {
        String file = args[0];
        String line = "";
        BufferedReader inFile = null;
        try {
            inFile = new BufferedReader(new FileReader(file));
            while ((line = inFile.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.print("File not found. Try again.");
        } catch (IOException e) {
            System.out.print("Problem reading file.");
        } finally {
            if (inFile != null) {
                try {
                    inFile.close();
                } catch (IOException e) {
                    System.out.println("Problem with file.");
                }
            }
        }
    }
}
