/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q029.javabasics;

/**
 *
 * @author cstan
 */
public class ATest {

    String global = "111";

    public int parse(String arg) {
        int value = 0;
        try {
            String global = arg;
            value = Integer.parseInt(global);
        } catch (Exception e) {
            System.out.println(e.getClass());
        }
        System.out.print(global + " " + value + " ");
        return value;
    }

    public static void main(String[] args) {
        ATest ct = new ATest();
        System.out.print(ct.parse("333"));
    }

}
