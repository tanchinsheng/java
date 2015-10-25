/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q455.inheritance;

/**
 *
 *
 * Which of the given options can be inserted at //1 and //2?
 */
class Game {

    public void play() throws Exception {
        System.out.println("Playing...");
    }
}

class Soccer extends Game {

    public void play(String ball) {
        System.out.println("Playing Soccer with " + ball);
    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Game g = new Soccer();
        // 1
        g.play();
        Soccer s = (Soccer) g;
        // 2
        s.play("cosco");
        s.play();
    }

}
