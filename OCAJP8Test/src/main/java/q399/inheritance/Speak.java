package q399.inheritance;

class GoodSpeak extends Speak implements Tone {

    public void up() {
        System.out.println("UP UP UP");
    }
}

interface Tone {

    void up();
}

public class Speak {

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
