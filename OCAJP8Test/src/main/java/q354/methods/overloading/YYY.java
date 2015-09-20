/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q354.methods.overloading;

/**
 *
 * Complete the following code by filling the two blanks -
 */
class XXX {

    public void m() throws Exception {
        throw new Exception();
    }
}

public class YYY extends XXX {

    /**
     * @param args the command line arguments
     */
    @Override
    public void m() {
    }

    public static void main(String[] args) {
        YYY obj = new YYY();
        obj.m();
    }

}
