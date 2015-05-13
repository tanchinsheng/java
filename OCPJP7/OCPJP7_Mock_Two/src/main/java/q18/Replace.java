/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q18;

public class Replace {

    public static void main(String[] args) {
        String talk = "Pick a little, talk a little, pick a little, talk a little, cheep cheep cheep, talk a lot, pick a little more";
        String eat = talk.replaceAll("talk", "eat").replace("cheep", "burp");
        System.out.println(eat);
    }

}
