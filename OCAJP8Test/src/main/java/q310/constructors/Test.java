package q310.constructors;

class X {

    public X() {
        System.out.println("In X");
    }
}

class Y extends X {

    public Y() {
        super(); // The explicit call to super(); in class Y is not required because the compiler puts a call to super();
        System.out.println("In Y");
    }
}

class Z extends Y {

    public Z() {
        System.out.println("In Z");
    }
}

public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Y y = new Z();
        System.out.println();
        Z z = new Z();
    }

}
