package q268.loop;

public class TestClass {

    public static void main(String args[]) {
        boolean b = false;
        int i = 1;
        do {
            i++;
        } while (b = !b);
        System.out.println(i);
        // The loop body is executed twice and the program will print 3.
    }

}
