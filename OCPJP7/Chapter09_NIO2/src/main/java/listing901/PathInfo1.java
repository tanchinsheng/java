package listing901;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.nio.file.Path;
import java.nio.file.Paths;

// Class to illustrate how to use Path interface and its methods
public class PathInfo1 {

    public static void main(String[] args) {
        // create a Path object by calling static method get() in Paths class
        Path testFilePath = Paths.get("D:\\test\\testfile.txt");
        //Path testFilePath = Paths.get(".\\testfile.txt");

        // retrieve basic information about path
        System.out.println("Printing file information: ");
        System.out.println("\t file name: " + testFilePath.getFileName());
        System.out.println("\t root of the path: " + testFilePath.getRoot());
        System.out.println("\t parent of the target: " + testFilePath.getParent());
        System.out.println("\t absolute path: " + testFilePath.toAbsolutePath());

        // print path elements
        System.out.println("Printing elements of the path: ");
        for (Path element : testFilePath) {
            System.out.println("\t path element: " + element);
        }
    }
}
