/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the trmplate in the editor.
 */
package q75;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Read {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\q75\\names.txt"));
        System.out.println(br.readLine());
        br.mark(100);
        System.out.println(br.readLine());
        br.reset();
        System.out.println(br.readLine());
    }

}
