/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q568.string;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String s = "blooper";
        StringBuilder sb = new StringBuilder(s);
        s.append("whopper"); //append() method does not exist in String class. It exits only in StringBuffer
        //and StringBuilder. The value of sb will be bloopershopper though.
        sb.append("shopper");
        System.out.println(s);
        System.out.println(sb);
    }

}
