package wildcards;

import java.util.List;

public class LowerBound {

    //supertypes of Integer, such as Integer, Number, and Object
    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
    }
}
