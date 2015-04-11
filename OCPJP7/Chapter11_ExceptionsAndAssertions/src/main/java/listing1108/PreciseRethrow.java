/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing1108;

/**
 *
 * @author cstan
 */
public class PreciseRethrow {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            foo();
            //} catch (Exception ife) {
        } catch (NumberFormatException ife) {
            System.out.println(ife);
        }
    }

    static private void foo() throws NumberFormatException {
        try {
            int i = Integer.parseInt("ten");
            //} catch (NumberFormatException ife) {
            //    throw ife;
//        } catch (Exception e) {
//            throw new NumberFormatException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

}
