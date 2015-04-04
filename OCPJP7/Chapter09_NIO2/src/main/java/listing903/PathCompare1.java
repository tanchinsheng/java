package listing903;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.nio.file.Path;
import java.nio.file.Paths;

class PathCompare1 {

    public static void main(String[] args) {
        Path path1 = Paths.get("Test");
        Path path2 = Paths.get("C:\\app\\Github\\java\\OCPJP7\\Chapter09_NIO2\\Test");
        // comparing two paths using compareTo() method
        System.out.println("(path1.compareTo(path2) == 0) is: " + (path1.compareTo(path2) == 0));

        //comparing two paths using equals() method
        System.out.println("path1.equals(path2) is: " + path1.equals(path2));

        // comparing two paths using equals() method with absolute path
        System.out.println("path2.equals(path1.toAbsolutePath()) is "
                + path2.equals(path1.toAbsolutePath()));
    }
}
