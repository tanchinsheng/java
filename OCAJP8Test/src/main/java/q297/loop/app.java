/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q297.loop;

/**
 *
 * INT1 and INT2 can be any two integers. Which of the following will produce
 * the same result?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        final int INT1 = 0, INT2 = 3;

        for (int i = INT1; i < INT2; i++) {
            System.out.print(i);
        }
        System.out.println();

        for (int i = INT1; i < INT2; System.out.print(++i));
        System.out.println();

        for (int i = INT1; i++ < INT2; System.out.print(i));
        System.out.println();

        int i1 = INT1;
        while (i1++ < INT2) {
            System.out.print(i1);
        }
        System.out.println();

        int i2 = INT1;
        do {
            System.out.print(i2);
        } while (i2++ < INT2);
        System.out.println();

    }

}
