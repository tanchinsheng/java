/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q155.operators.decision;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * & and | do not short circuit the expression but && and || do. As the
     * value of all the expressions ( 1 through 4) can be determined just by
     * looking at the first part, && and || do not evaluate the rest of the
     * expression, so method1() is not called for 2 and 4. Hence the value of i
     * is incremented only twice.
     */
    public static boolean method1(int i) {
        return i > 0 ? true : false;
    }

    public static void main(String args[]) {
        int i = 0;
        boolean bool1 = true;
        boolean bool2 = false;
        boolean bool = false;
        bool = (bool2 & method1(i++)); //1       
        bool = (bool2 && method1(i++)); //2       
        bool = (bool1 | method1(i++)); //3       
        bool = (bool1 || method1(i++)); //4       
        System.out.println(i);

    }

}
