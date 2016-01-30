package q123.javadatatypes.gc;

public class Test {

    void test() {
        MyClass obj = new MyClass();
        obj.name = "jack";
        // 1 insert code here   
        obj = null; // GC
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
