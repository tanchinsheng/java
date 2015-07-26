
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cstan
 */
public class SquareDance {

    public static void main(String[] args) {
        PriorityQueue<String> men = new PriorityQueue<>();
        PriorityQueue<String> women = new PriorityQueue<>();
        String line = null;
        String file = ".\\src\\main\\java\\dance.txt";
        String sex = "";
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            while ((line = input.readLine()) != null) {
                sex = line.substring(0, 1);
                if (sex.equals("M")) {
                    men.add(line.substring(2));
                } else {
                    women.add(line.substring(2));
                }
            }
        } catch (IOException e) {
            System.out.println("Error opening file");
        }
        while (!men.isEmpty() && !women.isEmpty()) {
            System.out.println("The dance partners will be: ");
            System.out.println(men.poll() + " and " + women.poll());
        }
        if (men.isEmpty()) {
            System.out.print("There are " + women.size());
            System.out.print(" women waiting to dance.");
            System.out.println(women.peek() + " is the next woman dancer");
        }
        if (women.isEmpty()) {
            System.out.print("There are " + men.size());
            System.out.print(" men waiting to dance. ");
            System.out.println(men.peek() + " is the next man dancer");
        }

    }
}
