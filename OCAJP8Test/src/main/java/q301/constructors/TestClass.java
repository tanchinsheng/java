package q301.constructors;

public class TestClass {

    long l1;

    public void TestClass(long pLong) {
        l1 = pLong;
    }  //(1)

    public static void main(String args[]) {
        TestClass a, b;
        a = new TestClass();  //(2)
        b = new TestClass(5);  //(3)
    }

}
