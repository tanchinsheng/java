/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package substring;

public class TestGC {

    private String largeString = new String(new byte[100000]);

    String getString() {
        return this.largeString.substring(0, 2);
    //return new String(this.largeString.substring(0,2));
    }

    public static void main(String[] args) {
        java.util.ArrayList<String> list = new java.util.ArrayList<String>();
        for (int i = 0; i < 8000; i++) {
            TestGC gc = new TestGC();
            list.add(gc.getString());
        }
    }
}
