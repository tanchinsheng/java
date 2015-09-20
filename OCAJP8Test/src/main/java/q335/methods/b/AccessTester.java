/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q335.methods.b;

import q335.methods.a.AccessTest;

/**
 *
 * @author cstan
 */
public class AccessTester extends AccessTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AccessTest ref1 = new AccessTest();
        ref1.d();
        AccessTester ref2 = new AccessTester();
        ref2.c();
        ref2.d();
        AccessTest ref3 = new AccessTester();
        ref3.d();
    }

}
