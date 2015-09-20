/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q367.methods.overloading;

/**
 *
 * What will be printed when the following code is compiled and run?
 */
public class LoadTest {

    /**
     * You cannot have more than one method in a class with the same signature.
     * Method signature includes method name and the argument list but does not
     * include return type. Therefore, the two getLoad() methods have the same
     * signature and will not compile. This shows that method overloading cannot
     * be done on the basis of the return types.
     */
    public int getLoad() {
        return 1;
    }

    public double getLoad() {
        return 3.0;
    }

    public static void main(String[] args) {
        LoadTest t = new LoadTest();
        int i = t.getLoad();
        double d = t.getLoad();
        System.out.println(i + d);
    }

}
