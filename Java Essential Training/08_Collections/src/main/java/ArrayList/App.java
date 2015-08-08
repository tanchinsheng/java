/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArrayList;

import java.util.ArrayList;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        System.out.println(list);
        list.add("C");
        System.out.println(list);
        list.remove(0);
        System.out.println(list);
        String state = list.get(1);
        System.out.println(state);
        int pos = list.indexOf("C");
        System.out.println(pos);
    }

}
