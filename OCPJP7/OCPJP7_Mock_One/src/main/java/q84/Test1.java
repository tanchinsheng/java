/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q84;

class MyThread1 extends Thread {

    public MyThread1(String name) {
        this.setName(name);
        start();
        System.out.println("in ctor " + getName());
    }

    @Override
    public void run() {
        System.out.println("in run " + getName());
    }

}

public class Test1 {

    public static void main(String[] args) {
        new MyThread1("oops");
    }

}
