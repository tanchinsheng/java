/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package linkedlist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cstan
 */
public class LinkedListExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /* 
         * ArrayList manage arrays internally [0][1][2]...
         */
        List<Integer> arrayList = new ArrayList<Integer>();
        
        /*
         * LinkedLists consists of elements where each element
         * has a reference to the previous and next element
         * [0]->[1]->[2]....
         *       <-    <-
         */
        List<Integer> linkedList = new LinkedList<Integer>();
        
        doTimings("Arraylist", arrayList);
        doTimings("Linkedlist", linkedList);
        
    }
    private static void doTimings(String type, List<Integer> list) {
        for (int i=0; i<1E5;i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();
        // Add items at end of list
        
        /*
        for (int i=0; i<1E5;i++) {
            list.add(i);
        } 
        */
        // SLOW
        for (int i=0; i<1E5;i++) {
            list.add(0,i);
        } 
        
        long end = System.currentTimeMillis();
        System.out.println("Time taken : " + (end- start)+ "ms for " + type);
        
        
        
        
    }
    
}
