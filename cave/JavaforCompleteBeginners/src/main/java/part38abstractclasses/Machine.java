/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part38abstractclasses;

public abstract class Machine{
    private int id;
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
     
    public abstract void start();
    public abstract void doStuff();
    public abstract void shutdown();
     
    public void run() {
        start();
        doStuff();
        shutdown();
    }
}