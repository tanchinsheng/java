package ocpjp.pretest;

public class ParseString1 {

    public static void main(String[] s) {
        String quote = "Never lend books-nobody ever returns them!";
        String[] words = quote.split(" ", 2);
// split strings based on the delimiter " " (space)
        for (String word : words) {
            System.out.println(word);
        }
    }
}