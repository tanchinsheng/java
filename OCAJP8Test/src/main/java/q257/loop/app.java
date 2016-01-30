package q257.loop;

public class app {

    void A() {
        while () { //The condition expression in a while header is required.
            break;
        }
    }

    void B() {
        do {
            break;
        } while (true);
    }

    void C() {
        if (true) {
            break;
        }
    }

    void D() {
        switch (1) { //You can use a constant in switch(...);
            default:
                break;
        }
    }

    void E() {
        for (; true;) {
            break;
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
