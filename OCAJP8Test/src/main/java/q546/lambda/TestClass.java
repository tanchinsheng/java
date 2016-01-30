/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q546.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static boolean checkList(List list, Predicate<List> p) {
        return p.test(list);
    }

    public static void main(String[] args) {
        //boolean b  = checkList(new ArrayList(), al -> al.isEmpty());
        //boolean b = checkList(new ArrayList(), ArrayList al -> al.isEmpty());
        //boolean b = checkList(new ArrayList(), al -> return al.size() == 0);
        //boolean b = checkList(new ArrayList(), al -> al.add("hello"));
        //boolean b = checkList(new ArrayList(), (ArrayList al) -> al.isEmpty());
        System.out.println(b);
    }

}
