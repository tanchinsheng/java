/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q397.inheritance;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * What will the following code print when compiled and run?
 */
public class ClassnameTest {

    /**
     * The getClass method always returns the Class object for the actual object
     * on which the method is called irrespective of the type of the reference.
     * Since s refers to an object of class String, s.getClass returns Class
     * object for String  and similarly list.getClass returns Class object for
     * ArrayList.
     */
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder("mrx");
        String s = sb.toString();
        list.add(s);
        System.out.println(s.getClass());
        System.out.println(list.getClass());
    }

}
