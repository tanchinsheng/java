package q453.inheritance;

class A {

    public int getCode() {
        return 2;
    }
}

class AA extends A {

    public void doStuff() {
    }
}

public class app {

    public static void main(String[] args) {
        A a = null;
        AA aa = null;

        a = (AA) aa;
        a = new AA();
        aa = new A();
        aa = (AA) a;
        aa = a;

        ((AA) a).doStuff(); //ok

    }

}
