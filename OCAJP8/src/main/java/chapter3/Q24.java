/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

import java.util.List;

public class Q24 {

    public static void main(String[] args) {
        String[] names = {"Tom", "Dick", "Harry"};
        List<String> list = names.asList();
        list.set(0, "Sue");
        System.out.println(names[0]);
    }

}
