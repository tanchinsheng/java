/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q434.inheritance;

/**
 *
 * Identify correct statements about the following code -
 */
interface Drink {

    default double getAlcoholPercent() {
        return 0.0;
    }

    static double computeAlcoholPercent() {
        return 0.0;
    }
}

interface ColdDrink extends Drink {

    String getName();
}

class CrazyDrink implements ColdDrink {

    // INSERT CODE HERE }
    public String getName() {
        return null;
    }
;

}

public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
