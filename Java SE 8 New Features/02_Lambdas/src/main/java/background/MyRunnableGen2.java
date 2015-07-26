/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package background;

/**
 *
 * @author cstan
 */
public class MyRunnableGen2 {

    public static void main(String[] args) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                System.out.println("Hello from Gen 2!");
            }

        };
        new Thread(r).start();
    }
}
