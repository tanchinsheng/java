/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q253.loop;

/**
 *
 * What will the following code print?
 */
public class app {

    /**
     * Because break JILL; would be valid only when it is within the block of
     * code under the scope of the label JILL. In this case, the scope of JILL
     * extends only up till System.out.println(c); and break JILL; is out of the
     * scope of the label.
     */
    void crazyLoop() {
        int c = 0;
        JACK:
        while (c < 8) {
            JILL:
            System.out.println(c);
            if (c > 3) {
                break JILL;
            } else {
                c++;
            }
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
