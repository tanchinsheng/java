/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._15;

abstract class AbstractBook {

    public String name = "abs";
}

interface Sleepy {

    public String name = "undefined";
}

class Book extends AbstractBook implements Sleepy {

    public String name;

    public Book(String name) {
        this.name = name; // LINE A
    }

    public static void main(String[] args) {
        //AbstractBook philosophyBook = new Book("Principia Mathematica");
        Sleepy philosophyBook = new Book("Principia Mathematica");
        System.out.println("The name of the book is " + philosophyBook.name); // LINE B
    }
}
