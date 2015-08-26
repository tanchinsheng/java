/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q097.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Boolean.parseBoolean(" true ");
        // Although this will return true but it is still not a valid answer because
        // parseBoolean returns a primitive and not a Boolean wrapper object.
        /*
         Boolean.parseBoolean("true");
         public static Boolean valueOf(boolean b) {
         return (b ? TRUE : FALSE);
         }
         */

        Boolean.valueOf(true);
        Boolean.valueOf("trUE");
        Boolean.TRUE;
    }

}
