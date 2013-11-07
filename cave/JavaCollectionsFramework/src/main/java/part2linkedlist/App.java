/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part2linkedlist;

/**
 * in this tutorial I explain when to use LinkedList rather than ArrayList; we
 * look at how the two classes work internally and I also explain a bit about
 * the List interface.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        /*
         * ArrayLists manage arrays internally.
         * [0][1][2][3][4][5] ....
         */
        List<Integer> arrayList = new ArrayList<>(); // default 10 items

        /*
         * LinkedLists consists of elements where each element
         * has a reference to the previous and next element
         * [0]->[1]->[2] ....
         *    <-   <-
         */
        List<Integer> linkedList = new LinkedList<>();

        doTimings("ArrayList", arrayList); // add begin - verylong. add end-fast
        doTimings("LinkedList", linkedList);// doesn't care where
    }

    private static void doTimings(String type, List<Integer> list) {
        for (int i = 0; i < 1E5; i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();

        /*
         // Add items at end of list
         for(int i=0; i<1E5; i++) {
         list.add(i);
         }
         */
        // Add items elsewhere in list
        for (int i = 0; i < 1E5; i++) {
            list.add(0, i);//end of list
        }
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms for " + type);
        //Time taken: 12911 ms for ArrayList
        //Time taken: 113 ms for LinkedList
    }

}
