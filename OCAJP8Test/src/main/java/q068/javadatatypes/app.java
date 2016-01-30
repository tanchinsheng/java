package q068.javadatatypes;

public class app {

    public static void main(String[] args) {
        int a = 5, b = 7, k1 = 0, k2 = 0, k3 = 0;
        Integer m = null;

        k1 = new Integer(a) + new Integer(b);//1
        System.out.println(k1);
        k2 = new Integer(a) + b; //2        
        System.out.println(k2);
        k3 = a + new Integer(b); //3
        System.out.println(k3);
        m = new Integer(a) + new Integer(b); //4
        System.out.println(m);
    }

}
