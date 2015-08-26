/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q111.javadatatypes;

/**
 *
 * You are creating two different Data objects in the code. The first one uses a
 * constructor that takes an integer and the second one uses a constructor that
 * takes a String. Thus, when you call showMe on the first object, it prints 10Y
 * because "Y" is the default value of y as per the given code. When you call
 * showMe on the second object, it prints 0Z because 0 is the default value of x
 * as per the given code.
 */
public class TestClass {

    /**
     * Note that + operator is overloaded for String. So if you have a String as
     * any operand for +, a new combined String will be created by concatenating
     * the values of both the operands. Therefore, x+y will result in a String
     * that concatenates integer x and String y.
     *
     */
    public static void main(String[] args) {
        new Data(10).showMe();
        new Data("Z").showMe();
    }

}
