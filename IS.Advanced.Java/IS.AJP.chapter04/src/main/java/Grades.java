
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cstan
 */
public class Grades {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashMap<String, Integer> grades = new HashMap<String, Integer>();
        grades.put("Smith", 88);
        grades.put("Brown", 100);
        grades.put("Jones", 77);
        grades.put("Green", 90);
        System.out.println("Smith's grade : " + grades.get("Smith"));
        System.out.println("Size of grades : " + grades.size());
        grades.remove("Brown");
        System.out.println("Size of grades : " + grades.size());
        if (grades.containsKey("Brown")) {
            grades.remove("Brown");
            System.out.println("Record sucessfully removed.");
        }
        System.out.println("Size of grades : " + grades.size());
    }

}
