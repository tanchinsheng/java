/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q18;

interface Aquatic {

    public default int getNumberOfGills(int input) {
        return 2;
    }
}

public class ClownFish implements Aquatic {

    public String getNumberOfGills() {
        return "4";
    }

    public String getNumberOfGills(int input) {
        return "6";
    }

    public static void main(String[] args) {
        System.out.println(new ClownFish().getNumberOfGills(-1));
    }
}
