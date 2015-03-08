/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packt;

/**
 *
 * @author cstan
 */
public class ExceptionDemo {

    public void foo3() {
        try {
            throw new Exception();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void foo2() {
        foo3();
    }

    public void foo1() {
        foo2();
    }

    public static void main(String args[]) {
        new ExceptionDemo().foo1();
    }
}
