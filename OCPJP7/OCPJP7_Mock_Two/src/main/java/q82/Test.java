/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q82;

class Phone {

    public enum State {

        ONCALL, IDLE, WAITING
    }
}

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Phone.State state = Phone.State.ONCALL;
    }

}
