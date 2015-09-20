/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q360.methods.overloading;

/**
 *
 * Given the following code, which method declarations can be inserted at line 1
 * without any problems?
 */
public class OverloadTest {

    /**
     * @param args the command line arguments
     */
    public int sum(int i1, int i2) {
        return i1 + i2;
    }

    // 1
    public int sum(int a, int b) { // Will cause duplicate method. Variable names don't matter. Only their types.
        return a + b;
    }

    public int sum(long i1, long i2) {
        return (int) i1;
    }

    public int sum(int i1, long i2) {
        return (int) i2;
    }

    public long sum(long i1, int i2) {
        return i1 + i2;
    }

    public long sum(int i1, int i2) {
        return i1 + i2;
    } // Only the return type is different so the compiler will complain about having duplicate method sum.

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
