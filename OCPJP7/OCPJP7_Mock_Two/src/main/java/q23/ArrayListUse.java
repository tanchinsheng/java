/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q23;

import java.util.ArrayList;

class ArrayListUse {

    static ArrayList<Integer> doSomething(ArrayList<Integer> values) {

        values.add(new Integer(10));
        ArrayList<Integer> tempList = new ArrayList<>(values);
        tempList.add(new Integer(15));
        return tempList;
    }

    public static void main(String[] args) {
        ArrayList<Integer> allValues = doSomething(new ArrayList<Integer>());
        System.out.println(allValues);
    }

}
