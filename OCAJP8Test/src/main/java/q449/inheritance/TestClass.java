/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q449.inheritance;

/**
 *
 * What will the following code print when compiled and run?
 */
class ABCD {

    int x = 10;
    static int y = 20;
}

class MNOP extends ABCD {

    int x = 30;
    static int y = 40;
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(new MNOP().x + ", " + new MNOP().y);
    }

}
