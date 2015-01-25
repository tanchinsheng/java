package it.txt.general.utils;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 26-giu-2006
 * Time: 10.12.59
 * To change this template use File | Settings | File Templates.
 */
public class AlphabeticalSort implements Comparator {

    /**
     * This inner class is used due to alphabetically order the directories and files to be shown
     * There is a little strange situation that happens when this code runs under unix:
     * Let's suppose that we have the following directories (or files): AUTO, AUTo, AUto, Auto, auto, BASTONE, Bastone, bastone
     * Probably the user expects that those directories will be presente in the order above... but under a unix environment the order
     * will be the following one:
     * AUTO, AUTo, AUto, Auto, BASTONE, Bastone, auto, bastone
     * (the same result that a user get if he execute the ls command from the machine...)
     * This problem does not happen because you it is not case sensitive so can't have more that one file/dir with the same name.
     */
        public int compare(Object str1, Object str2) {
            int result = -2;
            char [] s1Lower;
            char [] s2Lower;

            if (str1 != null && str2 != null) {

                String s1 = ((String) str1).toLowerCase();
                String s2 = ((String)str2).toLowerCase();


                s1Lower = (new CoolString(s1)).toCharArray();
                s2Lower = (new CoolString(s2)).toCharArray();

                int s1Length = s1Lower.length;
                int s2Length = s2Lower.length;
                int cicleLimit = s1Length;

                if (s1Length > s2Length) {
                    cicleLimit = s2Length;
                    result = -1;
                } else if (s2Length > s1Length) {
                    cicleLimit = s1Length;
                    result = -1;
                }

                int i = 0;
                char s1CurrentChar;
                char s2CurrentChar;

                while (i < cicleLimit) {
                    s1CurrentChar = s1Lower[i];
                    s2CurrentChar = s2Lower[i];
                    if ( s1CurrentChar < s2CurrentChar ) {
                        return -1;
                    } else if ( s1CurrentChar > s2CurrentChar ) {
                        return 1;
                    }
                    i++;
                }
                if ( s2Length == s1Length )
                    result = 0;

            } else {
                if (str1 == null)
                    result = -1;
                else
                    result = 1;
            }
            return result;
        }

}
