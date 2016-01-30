package q216.arrays;

public class SomeClass {

    public static void main(String args[]) {
        int size = 10;
        int[] arr = new int[size];
        // Here, all the array elements are initialized to have 0.
        for (int i = 0; i < size; ++i) {
            System.out.println(arr[i]);
            // It correctly will declare and initialize an array of length
            // 10 containing int values initialized to have 0.
        }
    }

}
