package q402.inheritance;

class A {

    //public XXX m1(int a) {
    public double m1(int a) {
        return a * 10 / 4 - 30;
    }
}

public class A2 extends A {

    //public double m1(int a) {
    public double m1(int a) {
        return a * 10 / 4.0;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
