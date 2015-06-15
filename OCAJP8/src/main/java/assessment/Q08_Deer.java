/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assessment;

public class Q08_Deer {

    public Q08_Deer() {
        System.out.print("Deer");
    }

    public Q08_Deer(int age) {
        System.out.print("DeerAge");
    }

    private boolean hasHorns() {
        return false;
    }

    public static void main(String[] args) {
        Q08_Deer deer = new Reindeer(5);
        System.out.println("," + deer.hasHorns());
    }

}

class Reindeer extends Q08_Deer {

    public Reindeer(int age) {
        System.out.print("Reindeer");
    }

    // looks like an overridden method, but it is actually a hidden
    // method since it is declared private in the parent class. Because the hidden method is
    // referenced in the parent class, the parent version is used
    public boolean hasHorns() {
        return true;
    }
}
