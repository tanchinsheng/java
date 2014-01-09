/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.pretest;

public class ReplaceSubString {

    static String s = "kanha is a good boy & he is the best";
    static int count, i;
    static char[] oldStr;
    static char[] newStr = new char[100];

    static void replace(String ss, String sou, String dest) {
        oldStr = s.toCharArray();
        int index = 0;
        for (int j = 0; j < oldStr.length; j++) {
            newStr[index++] = oldStr[j];
            count = 0;
            i = 0;
            if (sou.charAt(i) == oldStr[j]) {
                count++;
                int jjj = j;
                while (i < sou.length() - 1) {
                    if (sou.charAt(++i) == oldStr[++jjj]) {
                        count++;
                    }
                }
                if (count == sou.length()) {
                    int kk = 0;
                    j = jjj;
                    int ptr = --index;
                    for (int iii = ptr; iii < ptr + dest.length(); iii++) {
                        newStr[iii] = dest.charAt(kk++);
                        index++;
                    }

                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        replace(s, "kanha", "kanhaiya");
        for (char chh : newStr) {
            System.out.print(chh);
        }
    }
}