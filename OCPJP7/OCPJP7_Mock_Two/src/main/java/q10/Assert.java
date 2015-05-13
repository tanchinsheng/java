/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q10;

public class Assert {

    public static void main(String[] args) {
        try {
            assert false;
        } catch (RuntimeException re) {
            System.out.println("In the handler of RuntimeException");
        } catch (Exception e) {
            System.out.println("In the handler of Exception");
        } catch (Error ae) {
            System.out.println("In the handler of Error");
        } catch (Throwable t) {
            System.out.println("In the handler of Throwable");
        }
    }

}
