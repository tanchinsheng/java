/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q379.inheritance;

/**
 *
 * What will be the output of the following program ?
 */
class CorbaComponent {

    String ior;

    CorbaComponent() {
        startUp("IOR");
    }// The method selection is done on the basis of the actual class of the object (which is OrderManager here).
    //So OrderManager's startUp is called, which sets the ior variable to URL://IOR.

    void startUp(String s) {
        ior = s;
    }

    void print() {
        System.out.println(ior);
    }
}

class OrderManager extends CorbaComponent {

    OrderManager() {
    }

    void startUp(String s) {
        ior = getIORFromURL(s);
    }

    String getIORFromURL(String s) {
        return "URL://" + s;
    }
}

public class Application {

    /**
     * @param args the command line arguments
     */
    static void start(CorbaComponent cc) {
        cc.print();
    }

    public static void main(String[] args) {
        start(new OrderManager());
    }

}