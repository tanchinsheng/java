/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q399.inheritance;

/**
 *
 * What can be inserted in the code below so that it will print UP UP UP?
 */
class GoodSpeak extends Speak implements Tone {

    public void up() {
        System.out.println("UP UP UP");
    }
}

interface Tone {

    void up();
}

public class Speak {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Speak s = new GoodSpeak();
        //INSERT CODE HERE
        ((Tone) s).up();
        s.up(); // It will not compile because the class of reference s is Speak, which does not have the method up().
        ((GoodSpeak) s).up();
        (GoodSpeak) s.up(); //Incorrect syntax. It will not compile.
        (Tone) (GoodSpeak) s.up(); // Incorrect syntax. It will not compile.
        // The following would have been valid: ((Tone)(GoodSpeak)s).up();
    }

}
