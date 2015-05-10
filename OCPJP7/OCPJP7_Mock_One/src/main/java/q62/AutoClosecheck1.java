/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q62;

import java.io.Closeable;

public class AutoClosecheck1 {

    public static void main(String[] args) {
        try (AutoCloseable autoCloseableImpl = new AutoCloseableImpl();
                Closeable closeableImpl = new CloseableImpl()) {
        } catch (Exception ex) {
        } finally {
        }
    }
// opposite to the order of resources
//In CloseImpl.close()
//In AutoCloseImpl.close()
}
