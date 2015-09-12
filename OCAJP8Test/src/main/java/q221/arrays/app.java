package q221.arrays;

/**
 *
 * What will the following code print when compiled and run?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[][] array = {{0}, {0, 1}, {0, 1, 2}, {0, 1, 2, 3}, {0, 1, 2, 3, 4}};
        int[] arr1 = array[4];
        System.out.println(arr1[4][1]);
        // arr1 is an array of one dimension. But arr1[4][1] is trying to access it as a two dimensional array.
        // This will, therefore, not compile.
        System.out.println(array[4][1]);
    }

}
