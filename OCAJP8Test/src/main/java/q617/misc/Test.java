/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q617.misc;

/**
 *
 * @author cstan
 */
class Super {

    static {
        System.out.print("super ");
    }
}

class One {

    static {
        System.out.print("one ");
    }
}

class Two extends Super {

    static {
        System.out.print("two ");
    }
}

public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        One o = null;
        Two t = new Two();
    }

}
