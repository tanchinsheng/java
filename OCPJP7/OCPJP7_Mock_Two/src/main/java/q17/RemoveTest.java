/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q17;

import java.util.ArrayList;

public class RemoveTest {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(new Integer(2));
        list.add(1);
        list.add(5);
        list.remove(2);
        System.out.println(list);
    }

}
