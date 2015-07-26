package com.lynda.javatraining.characterstreams;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BufferedMain {

    public static void main(String[] args) {

        try (
                BufferedReader in = new BufferedReader(new FileReader("textfile.txt"));//Recommended
                //auto handle character set other than UTF8
                BufferedWriter out = new BufferedWriter(new FileWriter("newfile.txt"));) {
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            System.out.println("All done!");
        } catch (FileNotFoundException e) {
            Logger.getLogger(BufferedMain.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(BufferedMain.class.getName()).log(Level.SEVERE, null, e);
        }

    }
}
