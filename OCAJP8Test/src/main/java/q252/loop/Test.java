package q252.loop;

public class Test {

    public static void main(String args[]) {
        int var = 20, i = 0;
        do {
            while (true) {
                if (i++ > var) {
                    break;
                }
            }
        } while (i < var--);
        System.out.println(var);
    }

}
