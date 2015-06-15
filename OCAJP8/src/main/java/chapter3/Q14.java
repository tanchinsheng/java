/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

/**
 *
 * @author cstan
 */
public class Q14 {

    public static void main(String[] args) {
        StringBuilder puzzle = new StringBuilder("Java");
        // INSERT CODE HERE
        // puzzle.reverse(); //OK
        //puzzle.append("vaJ$").substring(0, 4); // does not change the value of a StringBuilder
        puzzle.append("vaJ$").delete(0, 3).deleteCharAt(puzzle.length() - 1);//OK
        //puzzle.append("vaJ$").delete(0, 3).deleteCharAt(puzzle.length());
        System.out.println(puzzle);
    }

}
