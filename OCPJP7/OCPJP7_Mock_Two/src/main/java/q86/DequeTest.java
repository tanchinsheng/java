/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q86;

import java.util.ArrayDeque;
import java.util.Deque;

public class DequeTest {

    public static void main(String[] args) {
        Deque<String> deque = new ArrayDeque<>(2);
        deque.addFirst("one ");
        deque.addFirst("two ");
        deque.addFirst("three ");
        System.out.println(deque.pollLast());
        System.out.println(deque.pollLast());
        System.out.println(deque.pollLast());
    }

}
