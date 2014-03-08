package qna.eight;

import java.util.Arrays;
import java.util.List;

public final class Algorithm {

    public static <T extends Object & Comparable<? super T>>
            T max(List<? extends T> list, int begin, int end) {

        T maxElem = list.get(begin);

        for (++begin; begin < end; ++begin) {
            if (maxElem.compareTo(list.get(begin)) < 0) {
                maxElem = list.get(begin);
            }
        }
        return maxElem;
    }

    public static void main(String[] args) {
        System.out.println(max(Arrays.asList(5, 1, 3, 4), 1, 4));
    }
}
