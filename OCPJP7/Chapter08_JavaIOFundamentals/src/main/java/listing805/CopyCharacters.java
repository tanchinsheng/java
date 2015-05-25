/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing805;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyCharacters {

    // Notice that both CopyBytes and CopyCharacters use an int variable to read to and write from.
    // However, in CopyCharacters, the int variable holds a character value in its last 16 bits;
    //  in CopyBytes, the int variable holds a byte value in its last 8 bits.
    public static void main(String[] args) throws IOException {

        FileReader fr = null;
        FileWriter fw = null;

        try {
            fr = new FileReader("xanadu.txt");
            fw = new FileWriter("characteroutput.txt");

            int c;
            while ((c = fr.read()) != -1) {
                fw.write(c);
            }
        } finally {
            if (fr != null) {
                fr.close();
            }
            if (fw != null) {
                fw.close();
            }
        }
    }
}
