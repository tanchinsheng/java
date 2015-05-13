/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q80;

class Test {

    public static void main(String[] args) {
        //String test = "I am preparing for OCPJP";
        String test = "I ";
        String[] tokens = test.split("\\S");
        //String[] tokens = test.split("\\s");
        //String[] tokens = test.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            System.out.println(tokens[i]);
        }
        System.out.println(tokens.length);
    }

}
