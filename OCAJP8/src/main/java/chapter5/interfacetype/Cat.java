/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.interfacetype;

public class Cat implements Walk, Run { // DOES NOT COMPILE

    public static void main(String[] args) {
        System.out.println(new Cat().getSpeed());
    }
}
