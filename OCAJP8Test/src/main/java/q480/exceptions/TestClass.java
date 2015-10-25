/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q480.exceptions;

/**
 *
 * What will be the output of the following program?
 */
class TestClass {

    /**
     * Since the method amethod() does not throw any exception, try is printed
     * and the control goes to finally which prints finally. After that out is
     * printed.
     */
    public static void amethod() {
    }

    public static void main(String[] args) {
        try {
            amethod();
            System.out.println("try");
        } catch (Exception e) {
            System.out.println("catch");
        } finally {
            System.out.println("finally");
        }
        System.out.println("out");
    }

}
