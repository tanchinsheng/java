/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._09;

class Penguin extends NonFlyer implements Birdie, Biped { // LINE B

    public void walk() {
        System.out.print("walk ");
    }
}
