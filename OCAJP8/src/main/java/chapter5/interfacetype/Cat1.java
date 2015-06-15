/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.interfacetype;

public class Cat1 implements Walk, Run {

    @Override
    public int getSpeed() {
        return 1;
    }

    public static void main(String[] args) {
        System.out.println(new Cat1().getSpeed());
    }
}
