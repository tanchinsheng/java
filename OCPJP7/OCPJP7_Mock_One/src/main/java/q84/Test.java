/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q84;

class MyThread extends Thread {

    public MyThread(String name) {
        this.setName(name);
        start();
        System.out.println("in ctor " + getName());
    }

    // overridden start(), run() never called
    @Override
    public void start() {
        System.out.println("in start " + getName());
    }

    @Override
    public void run() {
        System.out.println("in run " + getName());
    }

}

public class Test {

    public static void main(String[] args) {
        new MyThread("oops");
    }

}
