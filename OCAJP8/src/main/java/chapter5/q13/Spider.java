/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q13;

class Arthropod {

    public void printName(double input) {
        System.out.print("Arthropod");
    }
}

public class Spider extends Arthropod {

    public void printName(int input) {
        System.out.print("Spider");
    }

    public static void main(String[] args) {
        Spider spider = new Spider();
        spider.printName(4);
        spider.printName(9.0);
    }
}
