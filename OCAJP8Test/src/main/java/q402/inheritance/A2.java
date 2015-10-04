/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q402.inheritance;

/**
 *
 * What can be substituted for XXX and YYY so that it can compile without any
 * problems?
 */
class A {

    //public XXX m1(int a) {
    public double m1(int a) {
        return a * 10 / 4 - 30;
    }
}

public class A2 extends A {

    /**
     * @param args the command line arguments
     */
    //public double m1(int a) {
    public double m1(int a) {
        return a * 10 / 4.0;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
