package q256.loop;

import java.util.ArrayList;
import java.util.Collection;

public class app {

    public static void main(String[] args) {
        Object o = null;
        Collection c = new ArrayList();//valid collection object

        for (final Object o2 : c) {
        }

        int[][] ia = {{1}, {1}};//valid array

        for (int i : ia[0]) {
        }

    }

}
