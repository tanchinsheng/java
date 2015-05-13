/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q24;

public class OverLoad {

    private OverLoad(Object o) {
        System.out.println("Object");
    }

    private OverLoad(double[] arr) {
        System.out.println("double []");
    }

    private OverLoad() {
        System.out.println("void");
    }

    public static void main(String[] args) {
        new OverLoad(null);
    }

}
