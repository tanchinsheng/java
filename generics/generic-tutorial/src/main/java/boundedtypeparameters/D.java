package boundedtypeparameters;

class A {
}

interface B { /* ... */ }

interface C { /* ... */ }

//If one of the bounds is a class, it must be specified first
class D<T extends A & B & C> { /* ... */

//    public static <T> int countGreaterThan(T[] anArray, T elem) {
//    int count = 0;
//    for (T e : anArray)
//        if (e > elem)  // compiler error
//            ++count;
//    return count;
//}

    public static <T extends Comparable<T>> int countGreaterThan(T[] anArray, T elem) {
        int count = 0;
        for (T e : anArray) {
            if (e.compareTo(elem) > 0) {
                ++count;
            }
        }
        return count;
    }

}
//class D<T extends B & A & C> { /* ... */ }  // compile-time error
