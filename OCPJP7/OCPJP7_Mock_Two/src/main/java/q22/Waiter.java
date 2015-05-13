/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q22;

public class Waiter extends Thread {

    public static void main(String[] args) {
        new Waiter().start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting to wait");
            wait(1000);
            System.out.println("Done waiting, returning back");
        } catch (InterruptedException e) {
            System.out.println("Caught InterruptedException ");
        } catch (Exception e) {
            System.out.println("Caught Exception ");
        }
    }

}
