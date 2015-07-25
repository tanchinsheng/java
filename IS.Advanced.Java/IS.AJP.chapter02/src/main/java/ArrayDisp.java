
public class ArrayDisp {

    static <T> void display(T[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] != null) {
                System.out.println(arr[i] + " ");
            }
        }
    }

    public static void main(String[] args) {
        String[] names = new String[10];
        names[0] = "David";
        names[1] = "Cynthia";
        names[2] = "Raymond";
        names[3] = "Jennifer";
        names[4] = "Bryan";
        names[5] = "Clayton";
        names[6] = "Terrill";
        names[7] = "Danny";

        display(names);

        Integer[] numbers = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        display(numbers);
    }

}
