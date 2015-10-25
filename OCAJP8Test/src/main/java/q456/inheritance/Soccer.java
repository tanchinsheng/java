/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q456.inheritance;

/**
 *
 * What will the following program print when compiled and run?
 */
class Game {

    public void play() throws Exception {
        System.out.println("Playing...");
    }
}

public class Soccer extends Game {

    /**
     * @param args the command line arguments
     */
    public void play() {
        System.out.println("Playing Soccer...");
    }

    public static void main(String[] args) {
        Game g = new Soccer();
        g.play();
    }

}
