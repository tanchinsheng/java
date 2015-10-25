/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q484.exceptions;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    static String j = "";

    public static void method(int i) {
        try {
            if (i == 2) {
                throw new Exception();
            }
            j += "1";
        } catch (Exception e) {
            j += "2";
            return;
        } finally {
            j += "3";
        }
        j += "4";
    }

    public static void main(String[] args) {
        method(1);
        method(2);
        System.out.println(j);
    }

}
