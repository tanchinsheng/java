/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q03;

/**
 *
 * @author cstan
 */
public class q3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("This, that, etc.!");
        System.out.println(sb.replace(12, 15, "etcetera"));//This, that, etcetera.!
    }

}
