/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q014.javabasics;

/**
 *
 * Local variables can have same name as member variables. The local variables
 * will simply shadow the member variables with the same names. Declaration (4)
 * defines a static method that tries to access a variable named 'a' which is
 * not locally declared. Since the method is static, this access will only be
 * valid if variable 'a' is declared static within the class. Therefore
 * declarations (1) and (4) cannot occur in the same definition.
 */
public class app {

    int a; //  (1)
    //variable names must be different.
    static int a;    //  (2)

    int f() {
        return a;
    }    //  (3)

    static int f() {
        return a;
    }    //  (4)

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    }

}
