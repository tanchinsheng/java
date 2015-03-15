/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._12;

enum Cards {

    CLUB, SPADE, DIAMOND, HEARTS
};

class CardsEnumTest {

    public static void main(String[] args) {
        for (Cards card : Cards.values()) {
            System.out.print(card + " ");
        }
    }
}
