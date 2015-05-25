/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing805;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CopyLines {

    public static void main(String[] args) throws IOException {

        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            br = new BufferedReader(new FileReader("xanadu.txt"));
            pw = new PrintWriter(new FileWriter("characteroutput.txt"));

            String l;
            while ((l = br.readLine()) != null) {
                pw.println(l);
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
        }
    }
}
