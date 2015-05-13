/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q60;

import java.util.Locale;

class Test1 {

    public static void main(String[] args) {
        Locale locale1 = new Locale("en");
        Locale locale2 = new Locale("en", "in");
        Locale locale3 = new Locale("th", "TH", "TH");
        System.out.println(locale1 + " " + locale2 + " " + locale3);
    }

}
