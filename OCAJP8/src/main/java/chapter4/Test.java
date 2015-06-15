/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4;

import java.util.Arrays;

public class Test {

    final public void walk7() {
    }
    private static final int four;

    final static void m4() {
    }

    static final void m3() {
    }

    void final m5() {
    }

    void public m6() {
    }

    public void methodA() {
        return;
    }

    public void methodA() {
        return null;
    }

    public void methodD() {
    }

    public static void main(String[] args) {
        Arrays.asList("one"); // DOES NOT COMPILE
    }

}
