/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing1124;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cstan
 */
public class ChainedExceptionToLog {

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
        } catch (Exception cause) {
            try {
                Handler handler = new FileHandler("OutFile.log");
                Logger.getLogger("").addHandler(handler);

            } catch (IOException e) {
                Logger logger = Logger.getLogger("package.name");
                StackTraceElement elements[] = e.getStackTrace();
                for (int i = 0, n = elements.length; i < n; i++) {
                    logger.log(Level.WARNING, elements[i].getMethodName());
                }
            }
        }
    }

}
