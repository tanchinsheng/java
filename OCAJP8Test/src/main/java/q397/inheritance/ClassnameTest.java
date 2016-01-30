package q397.inheritance;

import java.util.ArrayList;
import java.util.List;

public class ClassnameTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder("mrx");
        String s = sb.toString();
        list.add(s);
        System.out.println(s.getClass());
        System.out.println(list.getClass());
    }

}
