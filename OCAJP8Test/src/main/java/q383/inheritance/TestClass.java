package q383.inheritance;

class A {

    public int i = 10;
    private int j = 20;
}

class B extends A {

    private int i = 30; //1    
    public int k = 40;
}

class C extends B {
}

public class TestClass {

    public static void main(String[] args) {

        C c = new C();
        System.out.println(c.i); //2       
        System.out.println(c.j); //3       
        System.out.println(c.k);
        System.out.println(((A) c).i);
    }

}
