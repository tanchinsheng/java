package q247.arrays;

public class app {

    public static void main(String[] args) {
        int[] i1[] = {{1, 2}, {1}, {}, {1, 2, 3}};
        //int i2[] = new int[2] {1, 2};
        //If you give the elements explicitly you can't give the size. So it should be just int[] { 1, 2 } or just { 1, 2 }
        int i3[][] = new int[][]{{1, 2, 3}, {4, 5, 6}};
        int i4[][] = {{1, 2}, new int[2]};
        // int i5[4] = {1, 2, 3, 4};
        // You cannot specify the size on left hand side .
    }

}
