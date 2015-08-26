/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q088.javadatatypes;

/**
 *
 * @author cstan
 */
public class Square {

    private double side = 0;  // LINE 2

    /**
     * side is not a global variable that you can access directly (Note that
     * Java doesn't have the concept of a global variable). side is a field in
     * Square class. So you need to specify which Square object's side you are
     * trying to access. An integer can be assigned to a double but not vice
     * versa.
     */
    public static void main(String[] args) {
        Square sq = new Square();  // LINE 5
        // side = 10;  // LINE 6
        sq.side = 10;
    }

}
