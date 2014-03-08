package qna.seven;

import java.util.Arrays;
import java.util.List;

public class Seven {

    public static void print(List<? extends Number> list) {
        for (Number n : list) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        print(Arrays.asList(1, 2, 3, 4));
    }

}
