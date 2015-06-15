/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q17;

public abstract class Whale {

    public abstract void dive() {
    }

    ;
public static void main(String[] args) {
        Whale whale = new Orca();
        whale.dive();
    }
}

class Orca extends Whale {

    public void dive(int depth) {
        System.out.println("Orca diving");
    }
}
