/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q111.javadatatypes;

/**
 *
 * @author cstan
 */
public class Data {

    private int x = 0;
    private String y = "Y";

    public Data(int k) {
        this.x = k;
    }

    public Data(String k) {
        this.y = k;
    }

    public void showMe() {
        System.out.println(x + y);
    }
}
