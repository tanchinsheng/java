
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
public class Line {

    public static void main(String[] args) {
        PriorityQueue<String> line = new PriorityQueue<>();
        line.add("David");
        line.add("Cynthia");
        line.add("Raymond");
        line.add("Bryan");
        line.add("Clayton");
        System.out.println("The line: ");
        line.stream().forEach((name) -> {
            System.out.println(name);
        });
        line.poll();
        System.out.println("The line: ");
        line.stream().forEach((name) -> {
            System.out.println(name);
        });
        line.remove("Raymond");
        System.out.println("The line: ");
        line.stream().forEach((name) -> {
            System.out.println(name);
        });
        System.out.println("The head: " + line.peek());
    }

}
