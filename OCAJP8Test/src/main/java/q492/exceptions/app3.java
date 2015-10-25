/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q492.exceptions;

/**
 *
 * @author cstan
 */
public class app3 {

    /**
     * but correct ans is No exception will be throw
     */
    public static void main(String[] args) {
        Class<?> forName = Class.forName("java.lang.String"); //java.lang.ClassNotFoundException; must be caught or declared to be thrown
    }

}
