/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */

package part45recursion;

/**
Recursion is a vital, if little-used, tool in every programmer’s toolbox of tricks. 
* While not specific to Java, due to several requests I decided to include it in this course, 
* since after all it is one of those things that programmers are supposed to learn. 
* Certain problem are very hard to solve without recursion and very easy to solve with it.
 */
public class App {
    public static void main(String[] args) {
        // E.g. 4! = 4*3*2*1 (factorial 4)
        System.out.println(factorial(5));
    }
    private static int factorial(int value) {
        //System.out.println(value);
        if(value == 1) {
            return 1;
        }
        return factorial(value - 1) * value;
    }
 
}
