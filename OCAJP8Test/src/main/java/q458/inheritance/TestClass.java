/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q458.inheritance;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * Which of the following Java code fragments will compile and execute without
 * throwing exceptions?
 */
class A implements Runnable {

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class B extends A implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        A a = new A();
        B b = new B();
        Object o = a;
        Observer ob = (Observer) o;
    }

}
