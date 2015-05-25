/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections.queue;

import java.util.LinkedList;
import java.util.Queue;

public class Countdown {

    public static void main(String[] args) throws InterruptedException {
        int time = 10;
        Queue<Integer> queue = new LinkedList<>();

        for (int i = time; i >= 0; i--) {
            queue.add(i);
        }

        while (!queue.isEmpty()) {
            System.out.println(queue.remove());
            Thread.sleep(1000);
        }
    }
}
