
public class InsertionSort {

    public static void main(String[] args) {
        final int size = 10;
        int[] data = new int[size];
        for (int i = 0; i < size; ++i) {
            data[i] = (int) (Math.random() * 100);
        }
        System.out.println("Before sorting: ");
        for (int i = 0; i < size; ++i) {
            System.out.println(data[i]);
        }
        for (int i = 0; i < size; ++i) {
            for (int j = i; j > 0; --j) {
                System.out.println("i = " + i + ", j = " + j);
                if (data[j - 1] > data[j]) {
                    int temp = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = temp;
                }
            }
        }
        System.out.println();
        System.out.println("After sorting: ");
        for (int i = 0; i < size; ++i) {
            System.out.println(data[i]);
        }
    }

}
