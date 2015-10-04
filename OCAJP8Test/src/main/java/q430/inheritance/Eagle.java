/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q430.inheritance;

/**
 *
 * What can be done to make this code compile?
 */
class Bird {

//    private Bird() {
//    }
    public Bird() {
    }
}

public class Eagle extends Bird {

    /**
     * Since the constructor of Bird is private, the subclass cannot access it
     * and therefore, it needs to be made public. protected or default access is
     * also valid.
     */
    public String name;

    public Eagle(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        System.out.println(new Eagle("Bald Eagle").name);
    }

}
