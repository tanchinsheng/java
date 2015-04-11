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
public class ExceptionTest {

    public static void foo() { //throws Exception
        try {
            throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException oob) {
            throw new Exception(oob);
        }
    }

    public static void main(String[] args) {
        try {
            foo();
        } catch (Exception re) {
            System.out.println(re.getCause());
        }
    }

}
