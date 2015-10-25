/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q328.methods;

import java.io.FileNotFoundException;

/**
 *
 * Which of the following methods does not return any value?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public doStuff1() throws FileNotFoundException, IllegalArgumentException {
        //valid code not shown
    }
    //It is missing the return type. Every method must have a return type specified in its declaration.
    //It could be a valid constructor though if the class is named doStuff
    // because the constructors don't return anything, not even void.

    public

    null doStuff2() throws FileNotFoundException, IllegalArgumentException {
        //valid code not shown
    }// null can be a return value not a return type because null is not a type.

    public doStuff3() {
        //valid code not shown
    } // This is not a valid method because there is no return type declared.
    //Although it can be a valid constructor if the name of the class is doStuff.

    public void doStuff4() throws FileNotFoundException, IllegalArgumentException {
        //valid code not shown
    } // A method that does not return anything should declare its return type as void.
    // Note that this is different from constructors.
    // A constructor also doesn't return anything but for a constructor,
    // you don't specify any return type at all. That is how a constructor is differentiated from a regular method.

    private doStuff5() {
        //valid code not shown
    }// This is not a valid method because there is no return type declared.
    // Although it can be a valid constructor if the name of the class is doStuff.

    public static void main(String[] args) {
        // TODO code application logic here
    }

}