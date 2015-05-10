/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q62;

import java.io.Closeable;
import java.io.IOException;

class CloseableImpl implements Closeable {

    @Override
    public void close() throws IOException {
        System.out.println("In CloseImpl.close()");
    }
}

class AutoCloseableImpl implements AutoCloseable {

    @Override
    public void close() throws Exception {
        System.out.println("In AutoCloseImpl.close()");
    }
}

public class AutoClosecheck {

    public static void main(String[] args) {
        try (Closeable closeableImpl = new CloseableImpl();
                AutoCloseable autoCloseableImpl = new AutoCloseableImpl()) {
        } catch (Exception ex) {
        } finally {
        }
    }

}
