package q162.operators.decision;

public class app {

    void test(byte x) {
        switch (x) {
            case 'a':   // 1
            case 256:   // 2
            case 0:     // 3
            default:   // 4
            case 80:    // 5
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
