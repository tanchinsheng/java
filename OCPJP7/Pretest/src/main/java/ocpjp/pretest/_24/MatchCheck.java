/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._24;

class MatchCheck {

    public static void main(String[] args) {
        String[] strings = {"Severity 1", "severity 2", "severity3", "severity five"};
        for (String str : strings) {
            if (!str.matches("^severity[\\s+][1–5]")) {
                System.out.println(str + " does not match");
            }
        }
    }
}
