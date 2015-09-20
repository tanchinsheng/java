/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q334.methods;

/**
 *
 * Which of the following code fragments are valid method declarations?
 */
public class app {

    /**
     * A valid method declaration MUST specify a return type, all other things
     * are optional.
     */



    void method1{ }//It does not have () after method1.

    void method2() {
    }

    void method3(void) {
    }//The keyword void is not a valid type for a parameter.

    method4

    {
    }//Methods must specify a return type and '( )'.
    // If the method does not want to return a value, it should specify void.

    method5(void) {
    } //If the method does not take any parameter, it should have empty brackets instead of void.

    void method6();

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
