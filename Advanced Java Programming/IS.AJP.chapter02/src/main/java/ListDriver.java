/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cstan
 */
public class ListDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<String> glist = new List<>(10);
        glist.add("milk");
        glist.add("eggs");
        System.out.println("Grocery list:  " + glist.toString());
        List<Integer> numbers = new List<>(5);
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        System.out.println("Numbers: " + numbers.toString());
    }

}
