/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q71;

class Test {

    private static int mem = 0;

    public static void foo() {
        try {
            ++mem;
        } catch (Exception e) {
        } finally {
            ++mem;
        }
    }

    public static void main(String[] args) {
        foo();
        System.out.println(mem);
    }

}
