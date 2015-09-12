/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q264.loop;

/**
 *
 * Which of these for statements are valid?
 */
public class ans {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        for (int i1 = 5; i1 = 0; i1--) {
        }

        int j2 = 5;
        // for (int i2 = 0, j2 += 5; i2 < j2 ; i++) {j2--;}

        int i3, j3;
        for (j3 = 10; i3 < j3; j3--) { // i3 not initialized
            i3 += 2;
        }

        int i4 = 10;
        for (; i4 > 0; i4--) {
        }

        for (int i5 = 0, j5 = 10; i5 < j5; i5++, --j5) {;
        }

    }

}
