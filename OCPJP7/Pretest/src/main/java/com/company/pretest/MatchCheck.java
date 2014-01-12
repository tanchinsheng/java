package com.company.pretest;

class MatchCheck {

    public static void main(String[] args) {
        String[] strings = {"Severity 1", "severity 2", "severity3", "severity five"};
        for (String str : strings) {
        //  [^xyz] Any character except x, y, or z (i.e., negation)
        //  \s A whitespace character
        //  [a-z] from a to z
            if (!str.matches("^severity[\\s+][1–5]")) {
                System.out.println(str + " does not match");
            }
        }
    }
}
