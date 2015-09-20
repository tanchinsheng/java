/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q358.methods.overloading;

/**
 *
 * What will be printed?
 */
public class ParamTest {

    /**
     * The call to printSum(1, 2) will be bound to printSum(int, int) because 1
     * and 2 are ints, which are exact match to int, int. Note that if
     * printSum(int, int) method were not there in the code, printSum(double,
     * double) would have been invoked instead of printSum(Integer, Integer)
     * because widening is preferred over boxing/unboxing. We advise you to run
     * this program and try out various combinations. The exam has questions on
     * this pattern.
     */
    public static void printSum(int a, int b) {
        System.out.println("In int " + (a + b));
    }

    public static void printSum(Integer a, Integer b) {
        System.out.println("In Integer " + (a + b));
    }

    public static void printSum(double a, double b) {
        System.out.println("In double " + (a + b));
    }

    public static void main(String[] args) {
        printSum(1, 2);
    }

}
