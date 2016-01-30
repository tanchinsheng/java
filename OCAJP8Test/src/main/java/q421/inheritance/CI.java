package q421.inheritance;

interface I1 {

    public default void m1() {
        System.out.println("in I1.m1");
    }
}

interface I2 {

    public default void m1() {
        System.out.println("in I2.m1");
    }
}

class C2 implements I1, I2 { //This class will not compile.

    public void m1() {
        System.out.println("in C2.m1");
    }
}

public class CI implements I1, I2 { //This class will not compile.

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
