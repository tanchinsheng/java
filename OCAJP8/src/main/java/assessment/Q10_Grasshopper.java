/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assessment;

public class Q10_Grasshopper {

    private String name;

    public Q10_Grasshopper(String n) {
        name = n;
    }

    public static void main(String[] args) {
        Q10_Grasshopper one = new Q10_Grasshopper("g1");
        Q10_Grasshopper two = new Q10_Grasshopper("g2");
        one = two;  // g1 is eligible for garbage collection
        two = null; // g1 is eligible for garbage collection
        one = null;
    }

}
