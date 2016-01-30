/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q009.javabasics;

/**
 *
 * The order of keywords for a static import must be "import static ... ". You
 * can either import all the static members using import static
 * java.lang.Integer.* or one specific member using import static
 * java.lang.Integer.MAX_VALUE; You must specify the full package name of the
 * class that you are importing (just like the regular import statement). So,
 * import static Integer.*; is wrong.
 */
// 1
import static java.lang.Integer.MAX_VALUE;
import static java.lang.System.out;

public class StaticImport {

    public StaticImport() {
        out.println(MAX_VALUE);
    }

    public static void main(String[] args) {
        new StaticImport();
    }
}
