/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q109.javadatatypes;

/**
 *
 * @author cstan
 */
public class InitClass {

    private static int loop = 15;
    static final int INTERVAL = 10;
    boolean flag;
    //line 1

    static {
        System.out.println("Static");
    }

    static {
        loop = 1;
    }

    static {
        loop += INTERVAL;
    }

    {
        flag = true;
        loop = 0;
    }

    static {
        INTERVAL = 10;
    }

    public static void main(String[] args) {

    }
}
