package q369.methods.overloading;

public class app {

    public int setVar(int a, int b, float c) {
        return 0;
    }

    public int setVar(int a, float b, int c) {
        return (int) (a + b + c);
    }

    public int setVar(int a, float b, int c) {
        return this(a, c, b);
    }//this( ... ) can only be called in a constructor and that too as a first statement.

    public int setVar(int x, int y, float z) {
        return x + y;
    }// It will not compile because it is same as the original method. The name of parameters do not matter.

    public float setVar(int a, int b, float c) {
        return c * a;
    }//It will not compile as it is same as the original method. The return type does not matter.

    public float setVar(int a) {
        return a;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
