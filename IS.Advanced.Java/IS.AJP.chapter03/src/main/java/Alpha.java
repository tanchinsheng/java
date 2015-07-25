
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cstan
 */
public class Alpha {

    public static void main(String[] args) {
        TreeSet<String> names = new TreeSet<>();
        names.add("Raymond");
        names.add("Mike");
        names.add("Bryan");
        names.add("Jennifer");
        names.add("Clayton");
        names.add("Terrill");
        System.out.println("Number of names is " + names.size());

        names.stream().forEach((name) -> {
            System.out.println(name);
        });
        System.out.println("Name after Jennifer: " + names.lower("Jennifer"));
        System.out.println("Name after Jennifer: " + names.higher("Jennifer"));
        System.out.println("First element: " + names.first());
        System.out.println("Last element: " + names.last());
    }

}
