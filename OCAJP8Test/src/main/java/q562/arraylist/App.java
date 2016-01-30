/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q562.arraylist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * Consider the following code to count objects and save the most recent object
 * ...
 */
public class App {

    /**
     * @param args the command line arguments
     */
    int i = 0;
    Object prevObject;

    public void saveObject(List e) {
        prevObject = e;
        i++;
    }

    public static void main(String[] args) {

        new App().saveObject(new ArrayList());

        Collection c = new ArrayList();
        new App().saveObject(c);

        List l = new ArrayList();
        new App().saveObject(l);

        new App().saveObject(null);

        new App().saveObject(0);

    }
}
