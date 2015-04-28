/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q33;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cstan
 */
class ListFromVarargs {

    public static <T> List<T> asList1(T... elements) {
        ArrayList<T> temp = new ArrayList<>();
        for (T element : elements) {
            temp.add(element);
        }
        return temp;
    }

    public static <T> List<?> asList2(T... elements) {
        ArrayList<?> temp = new ArrayList<>();
        for (T element : elements) {
            temp.add(element);
        }
        return temp;
    }

    public static <T> List<?> asList3(T... elements) {
        ArrayList<T> temp = new ArrayList<>();
        for (T element : elements) {
            temp.add(element);
        }
        return temp;
    }

    public static <T> List<?> asList4(T... elements) {
        List<T> temp = new ArrayList<T>();
        for (T element : elements) {
            temp.add(element);
        }
        return temp;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
