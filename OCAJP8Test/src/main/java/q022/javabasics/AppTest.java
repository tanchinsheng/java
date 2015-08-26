/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q022.javabasics;

/**
 *
 * @author cstan
 */
public class AppTest {

    /**
     * Since the question says, "...an instance of the class is not needed...",
     * the method has to be static. Also, as the question does not say that
     * other packages should not have access to the method so public or
     * protected is also correct.
     */
    public static void main(String[] args) {
        App.someMethod1();
        App.someMethod2();
        App.someMethod3();

    }

}
