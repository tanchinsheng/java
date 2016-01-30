package q243.arrays;

public class TableTest {

    static String[][] table;

    public static void main(String[] args) {
        String[] x = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String[] y1 = {"1", "2", "3", "4", "5"};
        String[] y2 = {"a", "b", "c"};
        table = new String[3][];
        table[0] = x;
        table[1] = y1;
        table[2] = y2;
        //INSERT CODE HERE
        for (String[] row : table) {
            System.out.print(row[row.length - 1]);
        }
    }

}
