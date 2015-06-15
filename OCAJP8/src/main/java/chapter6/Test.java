/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter6;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] animals = new String[0];
        System.out.println(animals[0]);

        String type = "moose";
        Object obj = type;
        Integer number = (Integer) obj;
    }

}
