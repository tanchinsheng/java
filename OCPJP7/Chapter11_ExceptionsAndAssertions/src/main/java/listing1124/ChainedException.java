/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing1124;

/**
 *
 * @author cstan
 */
public class ChainedException {

    public static void foo() {
        try {
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException oob) {
            RuntimeException re = new RuntimeException(oob); // oob : Throwable cause
            re.initCause(oob);
            throw re;
        }
    }

    public static void main(String[] args) {
        try {
            foo();
        } catch (Exception re) {
            System.out.println(re.getClass());
        }
    }

}
