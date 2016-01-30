package q367.methods.overloading;

public class LoadTest {

    public int getLoad() {
        return 1;
    }

    public double getLoad() {
        return 3.0;
    }

    public static void main(String[] args) {
        LoadTest t = new LoadTest();
        int i = t.getLoad();
        double d = t.getLoad();
        System.out.println(i + d);
    }

}
