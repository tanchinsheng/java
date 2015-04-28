/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q04;

/**
 *
 * @author cstan
 */
public class q4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Object nullObj = null;
        StringBuffer sb = new StringBuffer(10);
        sb.append("hello ");
        sb.append("world ");
        sb.append(nullObj);
        sb.insert(11, "!");
        System.out.println(sb);
    }

}
