/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q131.operators.decision;

/**
 *
 * What will be the output of the following class:
 */
public class TestClass {

    /**
     * Always remember that Strings are immutable, you cannot change them. In
     * this case, s refers to "aaa", and no matter what testRefs() method does,
     * the variable s of main() will keep pointing to the same string "aaa".
     * StringBuilder on the other hand, is mutable. So, initially sb is pointing
     * to a StringBuilder object containing "bbb". Its reference is passed to
     * the testRefs() method. In that method, we change the local variable str
     * to point to a new string "aaa"+"bbb" = "aaabbb". Then we append this to
     * sb. Therefore sb now contains "bbbaaabbb". Setting the local reference
     * str and sb (in method testRefs()) to null, does not affect the variables
     * s and sb of the main() method.
     */
    public void testRefs(String str, StringBuilder sb) {
        str = str + sb.toString();
        sb.append(str);
        str = null;
        sb = null;
    }

    public static void main(String[] args) {
        String s = "aaa";
        StringBuilder sb = new StringBuilder("bbb");
        new TestClass().testRefs(s, sb);
        System.out.println("s=" + s + " sb=" + sb);
    }
}
