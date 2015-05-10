/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q60;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String str = "Suneetha N.=9876543210, Pratish Patil=9898989898";
        Pattern pattern = Pattern.compile("(\\w+)(\\s\\w+)(=)(\\d{10})");
        Matcher matcher = pattern.matcher(str);
        String newStr = matcher.replaceAll(":$2,$1");
        System.out.println(newStr);
        String newStr2 = matcher.replaceAll("$4:$2,$1");
        System.out.println(newStr2);
        String newStr3 = matcher.replaceAll("$3");
        System.out.println(newStr3);
        String newStr4 = matcher.replaceAll("|$1|$2|$3|$4");
        System.out.println(newStr4);
    }

}
