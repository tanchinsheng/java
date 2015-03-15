/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._31;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author cstan
 */
public class wordPath {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Path wordpadPath = Paths.get("C:\\Program Files\\Windows NT\\Accessories\\wordpad.exe");
        //System.out.println(wordpadPath.subpath(1, 1));
        System.out.println("a: " + wordpadPath.subpath(1, 2));
        // beginIndex - the index of the first element, inclusive
        // endIndex - the index of the last element, exclusive
        System.out.println("b: " + wordpadPath.subpath(0, 1));
        System.out.println("c: " + wordpadPath.subpath(1, 1));
        System.out.println("d: " + wordpadPath.subpath(4, 16));
    }

}
