/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part38abstractclasses;

public class Camera extends Machine {
 
    @Override
    public void start() {
        System.out.println("Starting camera.");
    }
 
    @Override
    public void doStuff() {
        System.out.println("Taking a photo");     
    }
 
    @Override
    public void shutdown() {
        System.out.println("Shutting down the camera.");
         
    }
 
}