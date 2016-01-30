/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q570.string;

/**
 *
 * In line 1, "" + 5 + 6 => "5"+6 => "56" In line 2, 5 + "" +6  => "5"+6 => "56"
 * In line 3, 5 + 6 +"" => 11+"" => "11" In line 4, 5 + 6 => 11 => "11"
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("" + 5 + 6);//1: 56
        System.out.println(5 + "" + 6);// 2: 56
        System.out.println(5 + 6 + "");// 3: 11
        System.out.println(5 + 6);// 4: 11
    }

}
