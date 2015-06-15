/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.interfacetype;

public class Bunny1 implements Hop {

    public void printDetails() {
        System.out.println(Hop.getJumpHeight());
    }
}
