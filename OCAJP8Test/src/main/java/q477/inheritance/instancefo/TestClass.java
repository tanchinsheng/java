/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q477.inheritance.instancefo;

/**
 *
 * What will be printed when the above code is compiled and run?
 */
interface Flyer {
}

class Bird implements Flyer {
}

class Eagle extends Bird {
}

class Bat {
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Flyer f = new Eagle();
        Eagle e = new Eagle();
        Bat b = new Bat();

        if (f instanceof Bird) { // true
            System.out.println("f is a Bird");
        }
        if (e instanceof Flyer) { // true
            System.out.println("e is a Flyer");
        }
        if (b instanceof Flyer) {
            System.out.println("b is a Flyer");
        }
    }

}
