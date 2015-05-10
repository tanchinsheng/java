/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q77;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author cstan
 */
public class Test {

    public static void main(String[] args) {
        String srcFile = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(srcFile));
            br.getChannel();

        } catch (IOException ex) {
        }

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(srcFile, "rw+");
            raf.getChannel();
        } catch (IOException ex) {
        }

        FileInputStream ifr = null;
        try {
            ifr = new FileInputStream(srcFile);
            ifr.getChannel();
        } catch (IOException ex) {
        }

        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream("temp.data"));
            dis.getChannel();
        } catch (IOException ex) {
        }

    }
}
