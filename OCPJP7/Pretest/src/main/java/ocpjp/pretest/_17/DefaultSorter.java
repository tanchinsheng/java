/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._17;

import java.util.Arrays;

class DefaultSorter {

    public static void main(String[] args) {
        String[] brics = {"Brazil", "Russia", "India", "China"};
        Arrays.sort(brics, null); // LINE A
        for (String country : brics) {
            System.out.print(country + " ");
        }
    }
}
