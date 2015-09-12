/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q198.operators.decision;

/**
 *
 * What can be the return type of method getSwitch so that this program compiles
 * and runs without any problems?
 */
public class TestClass {

    /**
     * If you just consider the method getSwitch, any of int long float or
     * double will do. But the return value is used in the switch statement
     * later on. A switch condition cannot accept float, long, double, or
     * boolean. So only int is valid. The return type cannot be byte, short, or
     * char because the expression x - 20/x + x*x; returns an int.
     */
    // public static int getSwitch(int x) {
    public static int getSwitch(int x) {
        return x - 20 / x + x * x;
    }

    public static void main(String args[]) {
        switch (getSwitch(10)) {
            case 1:
            case 2:
            case 3:
            default:
                break;
        }
    }

}
