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
public class MyRunnableGen4 {

    public static void main(String[] args) {

        Runnable r = () -> System.out.println("Hello from Gen 4!");
        new Thread(r).start();
    }
}
