/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q554.arraylist;

import java.util.ArrayList;

/**
 *
 * Which import statement should be added to make it compile?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    ArrayList<String> al;

    public void init() {
        al = new ArrayList<>();
        al.add("Name 1");
        al.add("Name 2");
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.init();
        System.out.println("Size = " + tc.al.size());
    }

}
