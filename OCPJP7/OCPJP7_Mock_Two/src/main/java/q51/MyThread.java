/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q51;

public class MyThread extends Thread {

    public MyThread(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        play();
    }

    private void play() {
        System.out.print(getName());
        System.out.print(getName());
    }

    public static void main(String[] args) {
        Thread tableThread = new MyThread("Table");
        Thread tennisThread = new MyThread("Tennis");
        tableThread.start();
        tennisThread.start();
    }

}
