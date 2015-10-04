/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q431.inheritance;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author cstan
 */
public class CleanConnector extends PortConnector {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public CleanConnector(int port) throws IOException, FileNotFoundException, Exception {
        super(port);
    }

}
