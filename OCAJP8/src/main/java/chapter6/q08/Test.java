/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter6.q08;

public class Test {

    public int doSomething() {
        int a = 0, b = 0;
        try {
            return a / b;
        } catch (RuntimeException e) {
            return -1;
        } catch (ArithmeticException e) {
            return 0;
        } finally {
            System.out.print("done");
        }
    }

    public static void main(String[] args) {

    }

}
