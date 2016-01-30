package q211.operators.decision;

public class TestClass {

    public static boolean method1(String str) {
        System.out.println(str);
        return true;
    }

    public static void main(String args[]) {
        int i = 0;
        boolean bool1 = true;
        boolean bool2 = false;
        boolean bool = false;
        bool = (bool2 & method1("1"));  //1
        bool = (bool2 && method1("2"));  //2
        bool = (bool1 | method1("3"));  //3
        bool = (bool1 || method1("4"));  //4
    }

}
