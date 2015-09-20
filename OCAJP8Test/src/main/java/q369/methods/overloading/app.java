/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q369.methods.overloading;

/**
 *
 * Which of the following methods correctly overload the above method?
 */
public class app {

    /**
     * A method is said to be overloaded when the other method's name is same
     * and parameters ( either the number or their order) are different. Option
     * 2 is not valid Because of the line: return this(a, c, b); This is the
     * syntax of calling a constructor and not a method. It should have been:
     * return this.setVar(a, c, b);
     */
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
