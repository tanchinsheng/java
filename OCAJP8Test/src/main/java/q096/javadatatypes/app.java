/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q096.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (Boolean.parseBoolean("true") == true) {
            System.out.println("true1");
        }
        if (Boolean.parseBoolean("TrUe") == new Boolean(null)) {
            System.out.println("true2"); // false
        }
        if (new Boolean("TrUe") == new Boolean(true)) {
            System.out.println("true3");  // false
        }
        //if (new Boolean() == false); // no suitable constructor

        //Even though both the sides have a Boolean wrapper containing true,
        // the expression will yield false because they point to two different Boolean wrapper objects.
        // public static final Boolean TRUE = new Boolean(true);
        if (new Boolean("true") == Boolean.TRUE) {
            System.out.println("true4");  // false
        }
        if (new Boolean("no") == false) {
            System.out.println("true5");
        }
    }

}
