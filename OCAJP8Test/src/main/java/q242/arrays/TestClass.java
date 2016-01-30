package q242.arrays;

public class TestClass {

    int[][] matrix = new int[2][3];

    int a[] = {1, 2, 3};
    int b[] = {4, 5, 6};

    public int compute(int x, int y) {
        //1 : Insert Line of Code here
        return a[x] * b[y];
    }

    public void loadMatrix() {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                //2: Insert Line of Code here
                matrix[x][y] = compute(x, y);
            }
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
