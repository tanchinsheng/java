/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q53;

class EnumTest {

    enum Directions {

        North, East, West, South
    };

    enum Cards {

        Spade, Hearts, Club, Diamond
    };

    public static void main(String[] args) {
        System.out.println("Directions.East: " + Directions.East);
        System.out.println("Cards.Hearts: " + Cards.Hearts);
        System.out.println("equals: " + Directions.East.equals(Cards.Hearts));
        System.out.println("Directions.East.ordinal(): " + Directions.East.ordinal());
        System.out.println("Cards.Hearts.ordinal(): " + Cards.Hearts.ordinal());
        System.out.println("Ordinals: " + (Directions.East.ordinal() == Cards.Hearts.ordinal()));
    }

}
