/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q05;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundle_it_IT extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
        {"1", "Uno"},
        {"2", "Duo"},
        {"3", "Trie"}
    };

    public static void main(String[] args) {
        ResourceBundle resBundle = ResourceBundle.getBundle("q5.ResourceBundle", new Locale("it", "IT", ""));
        System.out.println(resBundle.getObject(new Integer(1).toString()));
    }

}
