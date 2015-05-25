/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing1111;

import static java.lang.System.err;

public class SuppressedExceptionDemoWithTryCatch {

    public static void memberFunction() throws Exception {
        try (DirtyResource resource = new DirtyResource()) {
            resource.accessResource();
        }
    }

    public static void memberFunction2() throws Exception {
        try (DirtyResource resource = new DirtyResource()) {
            resource.testResource();
        }
    }

    /**
     * Executable member function demonstrating suppressed exceptions using
     * try-with-resources
     */
    public static void main(String[] arguments) throws Exception {
        try {
            //memberFunction2();
            memberFunction();
        } catch (Exception ex) {
            err.println("Exception encountered: " + ex.toString());
            final Throwable[] suppressedExceptions = ex.getSuppressed();
            final int numSuppressed = suppressedExceptions.length;
            if (numSuppressed > 0) {
                err.println("\tThere are " + numSuppressed + " suppressed exceptions:");
                for (final Throwable exception : suppressedExceptions) {
                    err.println("\t\t" + exception.toString());
                }
            }
        }
    }
}
