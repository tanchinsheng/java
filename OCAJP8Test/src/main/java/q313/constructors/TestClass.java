package q313.constructors;

public class TestClass {

    int i, j;

    public TestClass getInstance() {
        return new TestClass();
    }  //1

    public void TestClass(int x, int y) {
        i = x;
        j = y;
    }     //2

    public TestClass TestClass() {
        return new TestClass();
    }    //3
    public

    ~TestClass() {
    }                     //4

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
