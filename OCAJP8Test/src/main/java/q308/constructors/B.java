package q308.constructors;

class A {

    int i;

    public A(int x) {
        this.i = x;
    }
}

public class B extends A {

    int j;

//    public B() {
//    }
//   public B(int y) {
//        j = y;
//    }
//    public B(int y) { // Correct
//        super(y * 2);
//        j = y;
//    }
//    public B(int y) {
//        i = y;
//        j = y * 2;
//    }
//    public B(int z) { //Correct
//        this(z, z);
//    }
    public B(int x, int y) {
        super(x);
        this.j = y;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
