/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q086.javadatatypes;

import java.util.ArrayList;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * Note that al is declared as ArrayList<Double>, therefore the add method
     * is typed to accept only a Double.
     */
    public static void main(String[] args) {
        ArrayList<Double> al = new ArrayList<>();
        //INSERT CODE HERE
        // You cannot box an int into a Double object.
        // al.add(111); // can't compile
        // indexOf's accepts Object as a parameter. Although 1.0 is not an object, it will be boxed into a Double object.
        // System.out.println(al.indexOf(1.0)); // compile
        // System.out.println(al.contains("string"));   // compile
        // ArrayList does not have a field named length.
        // It does have a method named size() though. So you can do: Double d = al.get(al.size());
        // It will compile but will throw IndexOutOfBoundsException at run time in this case
        // because al.size() will return 0 and al.get(0) will try to get the first element in the list.
        Double d = al.get(al.length);
    }
}
