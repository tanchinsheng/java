/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q194.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("asdf");
        String str1 = sb.toString();
        //StringBuilder's toString() will always return a new String object. So == will always return false in this case.
        String str2 = str1;
        // Since str1 and str2 both point to the same String object, == will return true.
        System.out.println(str1 == str2);
    }

}
