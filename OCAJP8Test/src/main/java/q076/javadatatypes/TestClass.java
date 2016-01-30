package q076.javadatatypes;

public class TestClass {

    public static void main(String[] args) {
        TestClass t1, t2, t3, t4;
        t1 = t2 = new TestClass();
        t3 = new TestClass();
        //two news => two objects. t1, t2, t3, t4 => 4 references.
    }

}
