/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q520.exceptions;

import java.io.IOException;

/**
 *
 * What two changes can you do, independent of each other, to make the following
 * code compile:
 */
 //assume appropriate imports
class PortConnector5 {

    public PortConnector5(int port) throws IOException {
        if (Math.random() > 0.5) {
            throw new IOException();
        }
        throw new RuntimeException();
    }
}

public class app5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            PortConnector5 pc = new PortConnector5(10);
            //} catch (RuntimeException re) {
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

}
