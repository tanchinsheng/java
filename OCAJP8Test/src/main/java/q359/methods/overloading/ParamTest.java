/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q359.methods.overloading;

/**
 *
 * What will be printed?
 */
public class ParamTest {

    /**
     * The call to printSum(1.0, 2.0) will be bound to printSum(double, double )
     * because 1.0 and 2.0 are double, which are exact match to double, double.
     * Note that if you call printSum(1, 2) , printSum(float, float) would have
     * been invoked instead of printSum(double, double) because a float is
     * closer than a double to an int. We advise you to run this program and try
     * out various combinations. The exam has questions on this pattern.
     */
    public static void printSum(double a, double b) {
        System.out.println("In double " + (a + b));
    }

    public static void printSum(float a, float b) {
        System.out.println("In float " + (a + b));
    }

    public static void main(String[] args) {
        printSum(1.0, 2.0);
    }

}
