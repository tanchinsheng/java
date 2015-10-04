/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q419.inheritance;

/**
 *
 * Which statements about the following code are correct?
 */
interface House {

    public default String getAddress() {
        return "101 Main Str";
    }
}

interface Bungalow extends House {

    public default String getAddress() {
        return "101 Smart Str";
    }
}

class MyHouse implements Bungalow, House {
}

public class TestClass {

    /**
     * There is no problem with the code. It is perfectly valid for a
     * subinterface to override a default method of the base interface. A class
     * that implements an interface can also override a default method. It is
     * valid for MyHouse to say that it implements Bungalow as well as House
     * even though House is redundant because Bungalow is a House anyway.
     */
    public static void main(String[] args) {
        House ci = new MyHouse();//1
        System.out.println(ci.getAddress());//2 : 101 Smart Str
    }

}
