package it.txt.general.utils;

/**
 * Classe di utilità generica sui vettori
 * User: fabio.furgiuele
 * Date: 21-apr-2005
 * Time: 14.40.01
 * To change this template use File | Settings | File Templates.
 */
public class ArrayUtils {
    /**
     * Ritorna la posizione di un valore Stringa in un array di stringhe
     *
     * @param value valore da ricercare
     * @param arr   array in cui effettuare la ricerca
     * @return La posizione della prima occorenza di value all'interno di arr, -1 se non viene trovata
     */
    public static int indexOf(String value, String[] arr) {
        if (arr != null && arr.length > 0) {
            for (int z = 0; z < arr.length; z++) {
                if (arr[z].equals(value)) {
                    return z;
                }
            }
        }
        return -1;
    }

    /**
     * Ritorna la posizione di un valore int in un array di int
     *
     * @param value valore da ricercare
     * @param arr   array in cui effettuare la ricerca
     * @return La posizione della prima occorenza di value all'interno di arr, -1 se non viene trovata
     */
    public static int indexOf(int value, int[] arr) {
        if (arr != null && arr.length > 0) {
            for (int z = 0; z < arr.length; z++) {
                if (arr[z] == value) {
                    return z;
                }
            }
        }
        return -1;
    }
}
