/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q27;

import java.util.Arrays;
import java.util.Comparator;

class CountryComparator implements Comparator<String> {

    @Override
    public int compare(String country1, String country2) {
        return country1.compareTo(country2);
    }
}

public class Sort {

    public static void main(String[] args) {
        String[] brics = {"Brazil", "Russia", "India", "China"};
        Arrays.sort(brics, null);
        for (String country : brics) {
            System.out.println(country + " ");
        }
    }

}
