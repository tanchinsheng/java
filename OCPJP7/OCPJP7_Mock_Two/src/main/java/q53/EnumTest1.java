/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q53;

class EnumTest1 {

    enum Directions {

        North, East, West, South
    };

    enum Cards {

        Spade, East, Club, Diamond
    };

    public static void main(String[] args) {
        System.out.println("Directions.East: " + Directions.East);
        System.out.println("Cards.Hearts: " + Cards.East);
        System.out.println("equals: " + Directions.East.equals(Cards.East));
        System.out.println("Directions.East.ordinal(): " + Directions.East.ordinal());
        System.out.println("Cards.Hearts.ordinal(): " + Cards.East.ordinal());
        System.out.println("Ordinals: " + (Directions.East.ordinal() == Cards.East.ordinal()));
    }

}
