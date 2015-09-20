/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q324.methods;

/**
 *
 * @author cstan
 */
public class Updater {

    /**
     * @param args the command line arguments
     */
    public int update(int a, int offset) {
        a = a + offset;
        return a;
    }

    public static void main(String[] args) {
        Updater u = new Updater();
        int a = 99;
        a = u.update(a, 111);
        System.out.println(a);
    }

}
