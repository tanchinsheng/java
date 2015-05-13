/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q14;

import java.util.PriorityQueue;

public class PQueueTest {

    public static void main(String[] args) {
        PriorityQueue<Integer> someValues = new PriorityQueue<>();
        someValues.add(new Integer(10));
        someValues.add(new Integer(15));
        someValues.add(new Integer(5));
        //someValues.add(null);
        Integer value;
        while ((value = someValues.poll()) != null) {
            System.out.println(value + " ");
        }

    }

}
