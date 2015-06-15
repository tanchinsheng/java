/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter6.q14;

import java.io.IOException;

/**
 *
 * @author cstan
 */
public class Test {

    public void ohNo() throws IOException {
        System.out.println("");
        // throw new Exception(); // Cannot compile
        //throw new IllegalArgumentException(); // RuntimeException need not be handled
        // throw new IOException(); // OK
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
