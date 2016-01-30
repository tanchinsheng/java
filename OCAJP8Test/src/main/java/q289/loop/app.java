package q289.loop;

public class app {

    void crazyLoop() {
        int c = 0;
        JACK:
        while (c < 8) {
            JILL:
            System.out.println(c);
            if (c > 3) {
                continue JACK;
            } else {
                c++;
            }
        }
        JACK1://only possible with/without label in while, do, for
        if (c > 3) {
            continue JACK1;//can't exist in if loop,
        } else {
            c++;
        }
    }

    public static void main(String[] args) {
        new app().crazyLoop();
    }

}
