/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q063.javadatatypes;

/**
 *
 * @author cstan
 */
public class X {

    /**
     * @param args the command line arguments
     */
    String s1;
    String s2 =

    'asdf'; //A string must be enclosed in double quotes ".
    String s3 = 'a'; // 'a' is a char. "a" is a String.
    String s4 = this.toString(); //Since every class directly or indirectly extends Object class
    // and since Object class has a toString() method, that toString() method will be invoked and
    //the String that it returns will be assigned to s.

    String s5 = asdf; //there is no variable asdf defined in the given class.

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
