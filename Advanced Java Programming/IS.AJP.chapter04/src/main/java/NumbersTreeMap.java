
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cstan
 */
public class NumbersTreeMap {

    public static void main(String[] args) {
        TreeMap<String, String> numbers = new TreeMap<>();
        numbers.put("David", "2333");
        numbers.put("Raymond", "2334");
        numbers.put("Cynthia", "2335");
        numbers.put("Bryan", "2336");
        System.out.println("Cynthia:" + numbers.get("Cynthia"));
        numbers.put("Jennifer", "2227");
        System.out.println("Size of numbers:" + numbers.size());
        numbers.remove("David");
        System.out.println("Size of numbers:" + numbers.size());

    }

}
