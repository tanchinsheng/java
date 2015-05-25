/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing901;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SeekableByteChannelTest {

    public static void main(String[] args) {
        // create a Path object by calling static method get() in Paths class
        Path file = Paths.get("testfile.txt");
        // Defaults to READ
        try (SeekableByteChannel sbc = Files.newByteChannel(file)) {
            ByteBuffer buf = ByteBuffer.allocate(10);

            // Read the bytes with the proper encoding for this platform.  If
            // you skip this step, you might see something that looks like
            // Chinese characters when you expect Latin-style characters.
            String encoding = System.getProperty("file.encoding");
            while (sbc.read(buf) > 0) {
                buf.rewind();
                System.out.print(Charset.forName(encoding).decode(buf));
                buf.flip();
            }
        } catch (IOException x) {
            System.out.println("caught exception: " + x);
        }

    }

}
