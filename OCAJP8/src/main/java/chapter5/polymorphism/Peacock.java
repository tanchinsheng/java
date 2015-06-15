/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.polymorphism;

public class Peacock extends Bird {

    @Override
    public String getName() {
        return "Peacock";
    }

    public static void main(String[] args) {
        Bird bird = new Peacock();
        //Peacock bird = new Peacock();//Same result
        bird.displayInformation();
    }
}
