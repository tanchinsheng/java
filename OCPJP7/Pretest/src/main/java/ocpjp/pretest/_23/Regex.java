package ocpjp.pretest._23;

import java.util.regex.Pattern;

class Regex {

    public static void main(String[] args) {
//        The pattern a*b+c{3} means match a zero or more times,
//        followed by b one or more times, and c exactly three times.
        String pattern = "a*b+c{3}";
        String[] strings = {"abc", "abbccc", "aabbcc", "aaabbbccc"};
        for (String str : strings) {
            //false true false true
            System.out.print(Pattern.matches(pattern, str) + " ");
        }
    }
}
