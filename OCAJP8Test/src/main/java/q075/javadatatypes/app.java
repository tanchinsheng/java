package q075.javadatatypes;

public class app {

    // transient  and volalile modifiers are only valid for member field
    // static and final are valid modifiers for both member field and method
    static int sa;
    final Object[] objArr = {null}; //Declares and defines an array of Objects of length 1.
    abstract int t; // Variables can't be declared as abstract or native.
    native int u;

    abstract void format();

    native void test();

    final static private double PI = 3.14159265358979323846;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
