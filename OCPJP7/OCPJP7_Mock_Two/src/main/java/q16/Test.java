/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q16;

import java.util.PriorityQueue;

class Task implements Comparable<Task> {

    int priority;

    public Task(int val) {
        priority = val;
    }

    @Override
    public int compareTo(Task that) {
        if (this.priority == that.priority) {
            return 0;
        } else if (this.priority > that.priority) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return new Integer(priority).toString();
    }

}

class Test {

    public static void main(String[] args) {
        PriorityQueue<Task> tasks = new PriorityQueue<>();
        tasks.add(new Task(10));
        tasks.add(new Task(15));
        tasks.add(new Task(5));
        Task task;
        while ((task = tasks.poll()) != null) {
            System.out.print(task + " ");
        }
    }

}
