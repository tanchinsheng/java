/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter6.q19;

public class Test {

    public static void main(String[] args) {
        try {
            System.out.println("work real hard");
            //} catch (IOException e) { //  IOException is never thrown in the body of try
            //} catch (IllegalArgumentException e) { // OK
            //} catch (RuntimeException e) { // always caught
        } catch (StackOverflowError e) {
        } catch (RuntimeException e) {
        }
    }

}
