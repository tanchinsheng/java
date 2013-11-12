/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part22inheritance;

public class Car extends Machine {
     
     
    @Override
    public void start() {
        System.out.println("Car started");
    }
 
    public void wipeWindShield() {
        System.out.println("Wiping windshield");
    }
     
    public void showInfo() {
        System.out.println("Car name: " + name);
    }
}