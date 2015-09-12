/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q281.loop;

/**
 *
 * What will the following code print?
 */
public class TestClass {

    /**
     * Note that looper() declares an automatic variable x, which shadows the
     * instance variable x. So when x = m; is executed, it is the local variable
     * x that is changed not the instance field x. So getX() never returns 0. If
     * you remove int x = 0; from looper(), it will print 0 and end.
     */
    int x = 5;

    int getX() {
        return x;
    }

    public void looper() {
        int x = 0;
        while ((x = getX()) != 0) {
            for (int m = 10; m >= 0; m--) {
                x = m;
                System.out.print(x + " ");
            }
            System.out.println();
        }  //This program will compile and run but will never terminate.
    }

    public static void main(String args[]) throws Exception {
        TestClass tc = new TestClass();
        tc.looper();
        System.out.println(tc.x);
    }

}
