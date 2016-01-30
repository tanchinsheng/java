package q316.methods;

class Wrapper {

    int w = 10;
}

public class TestClass {

    static Wrapper changeWrapper(Wrapper w) {
        w = new Wrapper();
        w.w += 9;
        return w;
    }

    public static void main(String[] args) {
        Wrapper w = new Wrapper();
        w.w = 20;
        changeWrapper(w);
        w.w += 30;
        System.out.println(w.w); // 50
        w = changeWrapper(w);
        System.out.println(w.w); //19
    }
}
