package listing1122;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// This interface is meant for implemented by classes that would read an integer from a file
interface IntReader {

    int readIntFromFile() throws IOException;
}

class ThrowsClause4 implements IntReader {
    // implement readIntFromFile with the same throws clause
    // or a more general throws clause

    @Override
    public int readIntFromFile() throws FileNotFoundException {
        Scanner consoleScanner = new Scanner(new File("integer.txt"));
        return consoleScanner.nextInt();
    }
	// main method elided in this code since the focus here is to understand
// issues related to overriding when throws clause is present
}
