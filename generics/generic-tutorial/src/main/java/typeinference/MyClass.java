package typeinference;

import java.util.Collections;
import java.util.List;

class MyClass<X> {

    <T> MyClass(T t) {
        // ...
    }

    static void processStringList(List<String> stringList) {
        // process stringList
    }

    public static void main(String[] args) {
        new MyClass<Integer>("");

        MyClass<Integer> myObject = new MyClass<>("");

        //List<Object> cannot be converted to List<String>
        //processStringList(Collections.emptyList());
        processStringList(Collections.<String>emptyList()); // JDK 7
        //processStringList(Collections.emptyList()); JDK 8
    }
}
