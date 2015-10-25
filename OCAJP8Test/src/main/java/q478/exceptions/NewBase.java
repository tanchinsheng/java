/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q478.exceptions;

/**
 *
 * Which statements regarding the following code are correct ?
 */
class Base {

    void method1() throws java.io.IOException, NullPointerException {
        someMethod("arguments");
        // some I/O operations    
    }

    int someMethod(String str) {
        if (str == null) {
            throw new NullPointerException();
        } else {
            return str.length();
        }
    }
}

public class NewBase extends Base {

    /**
     * @param args the command line arguments
     */
    void method1() {
        someMethod("args");
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
