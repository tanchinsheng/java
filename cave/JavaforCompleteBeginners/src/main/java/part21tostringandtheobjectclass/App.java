/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part21tostringandtheobjectclass;

/**
How to use the important toString() method in Java, and a bit about the Object class 
* that’s the grandfather of all objects in Java.
 */
class Frog {
    private final int id;
    private final String name;
    public Frog(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("%-4d: %s", id, name);
        /*
         StringBuilder sb = new StringBuilder();
         sb.append(id).append(": ").append(name);
         return sb.toString();
         */
    }
}

public class App {
    public static void main(String[] args) {
        Frog frog1 = new Frog(7, "Freddy");
        Frog frog2 = new Frog(5, "Roger");

        System.out.println(frog1);
        System.out.println(frog2);
    }
}
