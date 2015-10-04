/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q432.inheritance;

/**
 *
 * @author cstan
 */
class Super2 {

    static {
        System.out.print("Super ");
    }
}

class One {

    static {
        System.out.print("One ");
    }
}

class Two extends Super2 {

    static {
        System.out.print("Two ");
    }
}

public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        One o = null;
        Two t = new Two();
        System.out.println((Object) o == (Object) t);
    }

}
