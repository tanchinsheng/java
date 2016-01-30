package q433.inheritance;

interface I {

    int getI(int a, int b);
}

interface J {

    int getJ(int a, int b, int c);
}

abstract class MyIJ implements J, I {
}

class MyI {

    int getI(int x, int y) {
        return x + y;
    }
}

interface K extends J {

    int getJ(int a, int b, int c, int d);
}

public class app {

    public static void main(String[] args) {

    }

}
