/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q565.string;

/**
 *
 * What will be written to the standard output when the following program is
 * run?
 */
public class TrimTest {

    /**
     * It will print 13 !!! Note that line.concat("world") does not change line
     * itself. It creates a new String object containing " hello world" but it
     * is lost because there is no reference to it. Similarly, calling trim()
     * does not change the object itself. So the answer is 8 + 5 = 13 !
     */
    public static void main(String[] args) {

        String blank = " ";// one space
        String line = blank + "hello" + blank + blank;

        System.out.println(line.length());

        line.concat("world");

        System.out.println(line.length());
        String newLine = line.trim();

        System.out.println((int) (line.length() + newLine.length()));
    }

}
