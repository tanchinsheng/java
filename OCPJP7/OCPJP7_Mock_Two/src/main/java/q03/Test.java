/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q03;

public class Test {

    int a = 0;

    public static void print(int a) {
        this.a = a;
        System.out.println("a = " + this.a);
    }

    public static void main(String[] args) {
        Test obj = new Test();
        obj.print(10);
    }

}
