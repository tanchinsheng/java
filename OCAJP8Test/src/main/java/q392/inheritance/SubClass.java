package q392.inheritance;

class SuperClass {

    public SuperClass() {
    } // The default no args constructor will not be provided because
    //SuperClass has to define one arg constructor.

    public SuperClass(int a) {
    } // Because it is called in the second constructor of SubClass.
}

public class SubClass extends SuperClass {

    int i, j, k;

    public SubClass(int m, int n) {
        //super();
        i = m;
        j = m;
    } //1

    public SubClass(int m) {
        super(m);
    } //2

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
