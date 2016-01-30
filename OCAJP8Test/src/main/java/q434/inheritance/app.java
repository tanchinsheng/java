package q434.inheritance;

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

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
