/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q43;

import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author cstan
 */
public class Test {

    public static void main(String[] args) {
        Deque<Integer> deque = new LinkedList<>();
        deque.add(10);
        deque.add(20);
        deque.peek();
        deque.peek();
        deque.peek();
        System.out.println(deque);
    }

}
