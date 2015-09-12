/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q205.operators.decision;

/**
 *
 * Which line in the following code will cause the compilation to fail?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        work();//LINE 10
        int j = j1;//LINE 11
        // j1 has not been declared at this point. You cannot use a variable before it is declared.
        // Note that static and instance fields are always defined before everything else.
        // So even though x is declared after the work() method,
        // it will be initialized before the method is actually executed.
        int j1 = (double) x;//LINE 12
        // You cannot assign a double to an int without casting it to int.
        // For example, int j1 = (int) x; is valid.
        // but int j1 = (double) (int) x; or int j1 = x; are not valid.
    }

    public static void work() throws Exception {
        System.out.println(x);
    } //LINE 15     }      
    static double x;//19

}
