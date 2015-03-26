package listing6_27;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
// This program demonstrates the usage of Arrays class
import java.util.Arrays;

class PrintArray {

    public static void main(String[] args) {
        int[] intArray = {1, 2, 3, 4, 5};
        System.out.println("The array contents are:  " + Arrays.toString(intArray));
        //This method acts as bridge between array-based and collection-based APIs, in combination with Collection.toArray()
        System.out.println("The array contents are:  " + Arrays.asList(intArray));
    }
}
