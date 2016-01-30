/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q548.arraylist;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * What sequence of characters will the following program print?
 */
public class ListTest {

    /**
     * The sequence a, c, b is printed. add(1, "c") will insert 'c' between 'a'
     * and 'b' . sublist(1 , 1) will return an empty list. First, "a" and "b"
     * are appended to an empty list. Next, "c" is added between "a" and "b".
     * Then a new list s2 is created using the sublist view allowing access to
     * elements from index 1 to index 1(exclusive) (i.e. no elements ). Now, s2
     * is added to s1. So s1 remains :a, c, b
     *
     */
    public static void main(String[] args) {
        List s1 = new ArrayList();
        s1.add("a");
        s1.add("b");
        s1.add(1, "c");
        List s2 = new ArrayList(s1.subList(1, 1));
        s1.addAll(s2);
        System.out.println(s1);

    }

}
