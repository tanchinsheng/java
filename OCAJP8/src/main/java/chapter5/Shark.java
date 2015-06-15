/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5;

class Fish {

    protected int size;
    private int age;

    public Fish(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

}

public class Shark extends Fish {

    private int numberOfFins = 8;

    public Shark(int age) {
        super(age);
        this.size = 4;
    }

    public void displaySharkDetails1() {
        System.out.println("Shark with age: " + getAge());
        System.out.println(" and " + size + " meters long");
        System.out.println(" with " + numberOfFins + " fins");
    }

    public void displaySharkDetails2() {
        System.out.println("Shark with age: " + this.getAge());
        System.out.println(" and " + this.size + " meters long");
        System.out.println(" with " + this.numberOfFins + " fins");
    }

    public void displaySharkDetails3() {
        System.out.println("Shark with age: " + super.getAge());
        System.out.println(" and " + super.size + " meters long");
        System.out.println(" with " + this.numberOfFins + " fins");
    }

    public static void main(String[] args) {
        Shark shark1 = new Shark(1);
        shark1.displaySharkDetails1();
        Shark shark2 = new Shark(2);
        shark2.displaySharkDetails2();
        Shark shark3 = new Shark(3);
        shark3.displaySharkDetails3();
    }
}
