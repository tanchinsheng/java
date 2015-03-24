package listing6_17;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
// This program demonstrates the usage of TreeSet class
import java.util.Set;
import java.util.TreeSet;

class TreeSetTest {

    public static void main(String[] args) {
        String pangram = "the quick brown fox jumps over the lazy dog";
        Set<Character> aToZee = new TreeSet<>();
        for (char gram : pangram.toCharArray()) {
            aToZee.add(gram);
        }
        System.out.println("The pangram is: " + pangram);
        System.out.print("Sorted pangram characters are: " + aToZee);
    }
}
