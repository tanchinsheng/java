/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q21;

import java.util.ArrayList;

class TypeCheck {

    public static void main(String[] args) {
        Class c1 = new ArrayList<String>().getClass();
        Class c2 = ArrayList.class;
        System.out.println(c1 == c2);
    }

}
