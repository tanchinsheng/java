/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q03;

public class Frog implements CanHop {

    public static void main(String[] args) {
        Frog frog1 = new TurtleFrog();
        TurtleFrog frog2 = new TurtleFrog();
        BrazilianHornedFrog frog3 = new TurtleFrog();
        CanHop frog4 = new TurtleFrog();
        Object frog5 = new TurtleFrog();
        Long frog6 = new TurtleFrog();
    }
}
