package q363.methods.overloading;

class XXX {

    public void m() {
        throw new RuntimeException();
    }
}

class YYY extends XXX {

    public void m() throws Exception {
        throw new Exception();
    }
}

public class TestClass {

    public static void main(String[] args) {
        ______ obj = new ______();
        obj.m();
    }

}
