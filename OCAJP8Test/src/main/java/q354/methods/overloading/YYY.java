package q354.methods.overloading;

class XXX {

    public void m() throws Exception {
        throw new Exception();
    }
}

public class YYY extends XXX {

    @Override
    public void m() {
    }

    public static void main(String[] args) {
        XXX obj = new YYY();
        obj.m();
    }

}
