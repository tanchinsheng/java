/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package part11implementingiterable;

/**
 *
 * @author cstan
 */
public class App {

    public static void main(String[] args) {
        UrlLibrary urlLibrary = new UrlLibrary();
        for (String html : urlLibrary) {
            System.out.println(html.length());
            System.out.println(html);
        }
    }
}
