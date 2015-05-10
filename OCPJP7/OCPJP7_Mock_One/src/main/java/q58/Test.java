/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q58;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String str1 = "xxzz";
        String str2 = "xyz";
        String str3 = "yzz";
        Pattern pattern = Pattern.compile("(xx)*y?z{1,}");
        Matcher matcher = pattern.matcher(str1);
        System.out.println(matcher.matches());
        System.out.println(pattern.matcher(str2).matches());
        System.out.println(Pattern.compile("(xx)*y?z{1,}").matcher(str3).matches());
    }

}
