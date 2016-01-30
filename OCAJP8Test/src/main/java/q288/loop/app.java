package q288.loop;

public class app {

    public static void main(String[] args) {
        int i = 0, j = 11;
        do {
            if (i > j) {
                break;
            }
            j--;
        } while (++i < 5);
        System.out.println(i + "  " + j);
    }

}
