package q366.methods.overloading;

public class Noobs {

    public void m(int a) {
        System.out.println("In int ");
    }

    public void m(char c) {
        System.out.println("In char ");
    }

    public static void main(String[] args) {

        Noobs n = new Noobs();
        int a = 'a';
        char c = 6;
        n.m(a);
        n.m(c);
    }

}
