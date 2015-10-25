/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q486.exceptions;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    static int validMethod() {
        return 0;
    }

    static int callValidMethod() {
        return 0;
    }

    public static void main(String[] args) throws Exception {
        try {
            for (;;);
        } finally {
        } // A try block must be accompanied by either a catch block or a finally block or both.

        try {
            File f = new File("c:\\a.txt");
        } catch  {
            f = null;
        } // Invalid syntax for catch. A catch must have a exception: catch(SomeException se){ }

        int k = 0;
        try {
            k = callValidMethod();
        }
        System.out.println(k);
        catch      {
                    k = -1;
                } // There cannot be any thing between a catch or a finally block and the closing
        // brace of the previous try or catch block.

        try {
            try {
                Socket s = new ServerSocket(3030);
            } catch (Exception e) {
                s = new ServerSocket(4040);
            }
        } //The first try doesn't have any catch or finally block.
        // Further, the variable s is not in scope in the catch block.

        try {
            s = new ServerSocket(3030);
        } catch (Exception t) {
            t.printStackTrace();
        } catch (IOException e) {
            s = new ServerSocket(4040);
        } catch (Throwable t) {
            t.printStackTrace();
        } //You can have any number of catch blocks in any order but each catch must be of a different type.
        // Also, a catch for a subclass exception should occur before a catch block for the superclass exception.
        // Here, IOException is placed before Throwable, which is good but Exception is placed before IOException,
        // which is invalid and will not compile.

        int x = validMethod();
        try {
            if (x == 5) {
                throw new IOException();
            } else if (x == 6) {
                throw new Exception();
            }
        } finally {
            x = 8;
        }
        catch(Exception e){ x = 9; }
        //finally cannot occur before any catch block.

    }

}
