/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter6.q04;

public class Test {

    public static void main(String[] args) {
        Object o = 3;
        Object obj = new Integer(3);
        String str = (String) obj;
        System.out.println(str);
    }

}
