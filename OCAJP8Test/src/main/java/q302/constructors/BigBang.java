package q302.constructors;

abstract class Bang {

    //abstract void f();  //(0): If this line is inserted, then class BigBang will have to be declared abstract.
    final void g() {
    }
    //final    void h(){} //(1): It will fail because BigBang will try to override a final method.
    protected static int i;
    private int j;
}

final class BigBang extends Bang {

    public static void main(String args[]) {
        Bang mc = new BigBang();
    }

    void h() {
    }
    //void k(){ i++; } //(3)
    //void l(){ j++; } //(4): It will fail since the method will try to access a private member 'j' of the superclass.
    int m;
}
