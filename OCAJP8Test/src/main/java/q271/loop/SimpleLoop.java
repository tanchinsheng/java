package q271.loop;

public class SimpleLoop {

    public static void main(String[] args) {
        int i = 0, j = 10;
        int count = 0;
        while (i < j) {
            i++;
            j--;
            count++;
        }
        System.out.println(i + " " + j + " " + count);
    }

}
