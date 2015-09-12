/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q168.operators.decision;

/**
 *
 * What changes should be made so that the program will print 54321?
 */
public class LoopTest {

    /**
     * @param args the command line arguments
     */
    int k = 5;

    public boolean checkIt(int k) {
        return k-- > 0 ? true : false;
    }

    public void printThem() {
        while (checkIt(k)) {
            //System.out.print(k);
            System.out.print(k--); // 54321
        }
    }

    public static void main(String[] args) {
        new LoopTest().printThem();
    }

}
