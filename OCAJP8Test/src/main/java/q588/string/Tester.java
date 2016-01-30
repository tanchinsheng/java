/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q588.string;

/**
 *
 * What will the following code print when compiled and run?
 */
class StringWrapper {

    private String theVal;

    public StringWrapper(String str) {
        this.theVal = str;
    }
}

public class Tester {

    /**
     * Hello, q588.string.StringWrapper@19e0bfd Hello, How are you?
     */
    public static void main(String[] args) {

        StringWrapper sw = new StringWrapper("How are you?");
        StringBuilder sb = new StringBuilder("How are you?");
        System.out.println("Hello, " + sw);
        System.out.println("Hello, " + sb);
    }

}
