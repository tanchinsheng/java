/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._10;

class TestSwitch {

    public static void main(String[] args) {
        String[] cards = {"Club", "spade", " diamond ", "hearts"};
        for (String card : cards) {
            switch (card) {
                case "Club":
                    System.out.print(" club ");
                    break;
                case "Spade":
                    System.out.print(" spade ");
                    break;
                case "diamond":
                    System.out.print(" diamond ");
                    break;
                case "heart":
                    System.out.print(" heart ");
                    break;
                default:
                    System.out.print(" none ");
            }
        }
    }
}
