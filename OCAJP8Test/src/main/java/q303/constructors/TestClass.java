package q303.constructors;

public class TestClass {

    public TestClass(int a, int b) {
    } // 1

    public void TestClass(int a) {
    }   // 2

    public TestClass(String s); // 3 : Constructors cannot have empty bodies (i.e. they cannot be abstract)

    private TestClass(String s, int a) {
    }     //4: You can apply public, private, protected to a constructor. But not static, final, synchronized, native and abstract.

    public TestClass(String s1, String s2) {
    }

    ; //5 :The compiler ignores the extra semi-colon.

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
