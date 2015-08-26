/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q057.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * Every expression has a value, in this case the value of the expression is
     * the value that is assigned to the right hand side of the equation. k has
     * a value of 9 which is assigned to j and then to i. Another implication of
     * this is : boolean b = false; if( b = true) { System.out.println("TRUE");}
     * The above code is valid and will print TRUE. Because b = true has a
     * boolean value, which is what an if statement expects. Note that if( i =
     * 5) { ... } is not valid because the value of the expression i = 5 is an
     * int (5) and not a boolean.
     */
    public static void main(String args[]) {
        int i, j, k;
        i = j = k = 9;
        System.out.println(i);
    }

}
