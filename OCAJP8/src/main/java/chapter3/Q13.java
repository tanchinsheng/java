/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

/**
 *
 * @author cstan
 */
public class Q13 {

    public static void main(String[] args) {
        StringBuilder b = "rumble";
        b.append(4).deleteCharAt(3).delete(3, b.length() - 1);
        System.out.println(b);
    }

}
