/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q376.inheritance;

/**
 *
 * @author cstan
 */
class A {

    int max(int x, int y) {
        if (x > y) {
            return x;
        } else {
            return y;
        }
    }
}

class B extends A {

    int max(int x, int y) {
        return 2 * super.max(x, y);
    }
}

class C extends B {

    int max(int x, int y) {
        return super.max(2 * x, 2 * y);
    }
}

public class testClass {

    /**
     * When the program is run, the main() method will call the max() method in
     * C with parameters 10 and 20 because the actual object referenced by 'c'
     * is of class C. This method will call the max() method in B with the
     * parameters 20 and 40. The max() method in B will in turn call the max()
     * method in A with the parameters 20 and 40. The max() method in A will
     * return 40 to the max() method in B. The max() method in B will return 80
     * to the max() method in C. And finally the max() of C will return 80 in
     * main() which will be printed out.
     */
    public static void main(String[] args) {
        B c = new C();
        System.out.println(c.max(10, 20));
    }

}
