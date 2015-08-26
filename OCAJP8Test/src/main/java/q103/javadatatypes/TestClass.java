/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q103.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Square mysq = new Square(10);
        mysq.color = "red";

        //set mysq's side to 20
        mysq.setSide(20);
    }

}
