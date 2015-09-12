/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q308.constructors;

/**
 *
 * Given the following code, which of the constructors shown in the options can
 * be added to class B without causing a compilation to fail?
 */
class A {

    int i;

    public A(int x) {
        this.i = x;
    }
}

public class B extends A {

    /**
     * @param args the command line arguments
     */
    int j;

//    public B() {
//    }
//    public B(int y) {
//        j = y;
//    }
//    public B(int y) { // Correct
//        super(y * 2);
//        j = y;
//    }
//    public B(int y) {
//        i = y;
//        j = y * 2;
//    }
//    public B(int z) { //Correct
//        this(z, z);
//    }
    public B(int x, int y) {
        super(x);
        this.j = y;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
