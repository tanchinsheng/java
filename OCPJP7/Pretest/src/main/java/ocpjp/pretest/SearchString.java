package ocpjp.pretest;

public class SearchString {

    public static void main(String[] args) {
        String quote = "An *onion* a day keeps everyone away!";
// match the word delimited by *'s
        int startDelimit = quote.indexOf('*');
        int endDelimit = quote.lastIndexOf("*");
        System.out.println(quote.substring(startDelimit, endDelimit));
    }
}