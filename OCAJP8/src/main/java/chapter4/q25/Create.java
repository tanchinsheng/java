/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.q25;

public class Create {

    Create() {
        System.out.print("1 ");
    }

    Create(int num) {
        System.out.print("2 ");
    }

    Create(Integer num) {
        System.out.print("3 ");
    }

    Create(Object num) {
        System.out.print("4 ");
    }

    Create(int... nums) {
        System.out.print("5 ");
    }

    public static void main(String[] args) {
        new Create(100);
        new Create(1000L);
    }
}
