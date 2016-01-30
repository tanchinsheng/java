package q416.inheritance;

class Parent {

    public Object myMethod() {
        System.out.println("");
        return new Object();
    }
}

public class Test extends Parent {

    public Integer myMethod() {
        System.out.println("");
        return 1;
    }

    public static void main(String[] args) {

    }

}
