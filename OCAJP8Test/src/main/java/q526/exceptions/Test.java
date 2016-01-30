/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q526.exceptions;

/**
 *
 * What will be the output when the following code is compiled and run?
 */
class E1 extends Exception {
}

class E2 extends E1 {
}

public class Test {

    /**
     * Since E2 is a sub class of E1, catch(E1 e) will be able to catch
     * exceptions of class E2. Therefore, E1 is printed. Once the exception is
     * caught the rest of the catch blocks at the same level (that is the ones
     * associated with the same try block) are ignored. So E is not printed.
     * finally is always executed (except in case of System.exit()), so Finally
     * is also printed.
     */
    public static void main(String[] args) {
        try {
            throw new E2();
        } catch (E1 e) {
            System.out.println("E1");
        } catch (Exception e) {
            System.out.println("E");
        } finally {
            System.out.println("Finally");
        }
    }

}
