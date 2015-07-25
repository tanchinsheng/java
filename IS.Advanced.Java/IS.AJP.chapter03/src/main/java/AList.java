
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cstan
 */
public class AList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Integer> grades = new ArrayList<>();
        grades.add(100);
        grades.add(90);
        grades.add(80);
        grades.add(70);
        grades.add(60);
        int total = 0;
        int gradesSize = grades.size();
        for (int i = 0; i < gradesSize; ++i) {
            total += grades.get(i);
        }

        double average = total / gradesSize;
        System.out.println("Size of grades: " + gradesSize);
        System.out.println("Average: " + average);

        total = 0;
        for (Integer grade : grades) {
            total += grade;
        }
        System.out.println("Size of grades (foreach): " + gradesSize);
        System.out.println("Average (foreach): " + total / gradesSize);

        total = 0;
        total = grades.stream().map((grade) -> grade).reduce(total, Integer::sum);
        System.out.println("Size of grades (funcational): " + gradesSize);
        System.out.println("Average (funcational): " + total / gradesSize);

        grades.remove(4);
        System.out.println("New size of grades: " + grades.size());

        grades.add(60);
        grades.add(50);
        System.out.println("Newer size of grades: " + grades.size());

    }

}
