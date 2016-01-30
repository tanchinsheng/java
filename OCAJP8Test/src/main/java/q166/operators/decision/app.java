package q166.operators.decision;

public class app {

    public static void main(String[] args) {
        int i = 1;
        int j = i++;
        if ((i == ++j) | (i++ == j)) {
            i += j;
        }
        System.out.println(i);
    }

}
