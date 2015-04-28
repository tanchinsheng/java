/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q51;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cstan
 */
public class AsList {

    public static void main(String[] args) {
        String hello = "hello";
        String world = "world";
        StringBuffer helloWorld = new StringBuffer(hello + world);
        List<String> list = Arrays.asList(hello, world, helloWorld.toString());
        helloWorld.append("!");
        list.remove(0);
        System.out.println(list);
    }

}
