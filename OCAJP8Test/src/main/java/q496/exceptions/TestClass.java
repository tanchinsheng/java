/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q496.exceptions;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    static void m1() {
        System.out.println("m1 Starts");
        throw new IndexOutOfBoundsException("Big Bang ");
    }

    public static void main(String[] args) {

        try {
            m1();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("1");
            throw new NullPointerException();
        } catch (NullPointerException e) {
            System.out.println("2");
            return;
        } catch (Exception e) {
            System.out.println("3");
        } finally {
            System.out.println("4");
        }
        System.out.println("END");
    }

}
