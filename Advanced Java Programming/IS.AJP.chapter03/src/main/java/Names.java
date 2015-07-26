
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Names {

    public static void main(String[] args) {
        HashSet<String> names = new HashSet<>();
        names.add("David");
        names.add("Bryan");
        names.add("Cynthia");
        System.out.println("The number of names is " + names.size());
//        for (String name : names) {
//            System.out.println(name);
//        }
        names.stream().forEach((name) -> {
            System.out.println(name);
        });
        names.remove("David");
        names.add("Raymond");
        String name = "David";
        if (names.contains(name)) {
            System.out.println(name + " is in the collections");
        } else {
            System.out.println(name + " is not in the collections.");
        }
        names.stream().forEach((n) -> {
            System.out.println(n);
        });
        names.clear();
        System.out.println("The number of names is " + names.size());
    }

}
