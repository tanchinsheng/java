/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q34;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateType {

    public static void main(String[] args) {
        List<Map<List<Integer>, List<String>>> list = new ArrayList<>();
        Map<List<Integer>, List<String>> map = new HashMap<>();
        list.add(null); // ADD_NULL
        list.add(map);
        list.add(new HashMap<List<Integer>, List<String>>()); //ADD_HASHMAP
        for (Map element : list) { // ITERATE
            System.out.println(element + " ");
        }
    }

}
