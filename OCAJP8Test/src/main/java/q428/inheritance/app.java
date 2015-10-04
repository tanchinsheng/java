/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q428.inheritance;

/**
 *
 * Given the following code, which statements are true?
 */
interface Automobile {

    String describe();
}

class FourWheeler implements Automobile {

    String name;

    public String describe() {
        return " 4 Wheeler " + name;
    }
}

class TwoWheeler extends FourWheeler {

    String name;

    public String describe() {
        return " 2 Wheeler " + name;
    }
}

public class app {

    /**
     * The use of inheritance in this code is not justifiable, since
     * conceptually, a TwoWheeler is-not-a FourWheeler.
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
