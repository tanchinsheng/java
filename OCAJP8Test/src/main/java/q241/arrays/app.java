package q241.arrays;

import java.util.ArrayList;

public class app {

    public static void main(String[] args) {
        Object o1 = new Object[]{"aaa", new Object(), new ArrayList(), 10};

        Object o2 = new Object[]{"aaa", new Object(), new ArrayList(), {}};
        Object o3 = new Object[]{"aaa", new Object(), new ArrayList(), new String[]{""}};

        Object o4 = new Object[1]
        {
            new Object()
        };
    }

}
