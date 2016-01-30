package q386.inheritance;

class B {
}

class B1 extends B {
}

class B2 extends B {
}

public class ExtendsTest {

    public static void main(String[] args) {
        B b = new B();
        B1 b1 = new B1();
        B2 b2 = new B2();
        // insert statement here
        b = b1; // There won't be a problem anytime because B1 is a B
        //b2 = b; // It fails at Compile time as an object referenced by b may not be a B2, so an explicit cast will be needed.
        b1 = (B1) b; //It will pass at compile time but fail at run time as the actual object referenced by b is not a B1.
        //b2 = (B2) b1; // It will not compile because b1 can never point to an object of class B2.
        //b1 = (B) b1; // This won't compile. Another cast is needed. i.e. b1 = (B1) (B) b1;
        //b1 = (B1) (B) b1;
    }

}
