package Iterators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

public class App {

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        list.add("California");
        list.add("Oregon");
        list.add("Washington");
        System.out.println(list);
        ListIterator<String> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            String value = listIterator.next();
            System.out.println(value);
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("California", "Sacramento");
        map.put("Oregon", "Salem");
        map.put("Washington", "Olympia");
        System.out.println(map);
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String keyValue = iterator.next();
            System.out.println("The capitol of " + keyValue + " is " + map.get(keyValue));
        }

    }

}
