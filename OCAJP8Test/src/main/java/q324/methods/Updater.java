package q324.methods;

public class Updater {

    public int update(int a, int offset) {
        a = a + offset;
        return a;
    }

    public static void main(String[] args) {
        Updater u = new Updater();
        int a = 99;
        a = u.update(a, 111);
        System.out.println(a);
    }

}
