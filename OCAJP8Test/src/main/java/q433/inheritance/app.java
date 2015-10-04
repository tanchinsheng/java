/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q433.inheritance;

/**
 *
 * @author cstan
 */
interface I {

    int getI(int a, int b);
}

interface J {

    int getJ(int a, int b, int c);
}

abstract class MyIJ implements J, I {
}

class MyI {

    int getI(int x, int y) {
        return x + y;
    }
}

interface K extends J {

    int getJ(int a, int b, int c, int d);
}

public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
