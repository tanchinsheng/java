package listing6_18;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//This program shows the usage of HashMap class

public class SpellChecker {

    public static void main(String[] args) {
        Map<String, String> misspeltWords = new HashMap<>();
        misspeltWords.put("calender", "calendar");
        misspeltWords.put("tomatos", "tomatoes");
        misspeltWords.put("existance", "existence");
        misspeltWords.put("aquaintance", "acquaintance");
        String sentence = "Buy a calender for the year 2013";
        System.out.println("The given sentence is: " + sentence);
        for (String word : sentence.split("\\W+")) {
            if (misspeltWords.containsKey(word)) {
                System.out.println("The correct spelling for " + word
                        + " is: " + misspeltWords.get(word));
            }
        }
        Set<String> keys = misspeltWords.keySet();
        System.out.println("Misspelt words in spellchecker are: " + keys);
    }
}
