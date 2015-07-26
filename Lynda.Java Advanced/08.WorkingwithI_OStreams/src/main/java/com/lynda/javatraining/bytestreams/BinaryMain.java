package com.lynda.javatraining.bytestreams;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryMain {

    public static void main(String[] args) {
        try (FileInputStream in = new FileInputStream("flower.jpg");
                FileOutputStream out = new FileOutputStream("new.jpg")) {

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }

        } catch (FileNotFoundException fnfe) {
            Logger.getLogger(BinaryMain.class.getName()).log(Level.SEVERE, "No files!", fnfe);
        } catch (IOException ioe) {
            Logger.getLogger(BinaryMain.class.getName()).log(Level.SEVERE, "IO error!", ioe);
        }

    }
}
