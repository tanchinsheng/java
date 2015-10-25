/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q465.inheritance;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * How can you fix the following code to make it compile:
 *
 */
class Great {

    //public void doStuff() throws IOException { //ok
    public void doStuff() throws FileNotFoundException {
    }
}

class Amazing extends Great {

    public void doStuff() throws IOException, IllegalArgumentException {
    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Great g = new Amazing();
        g.doStuff();
    }

}
