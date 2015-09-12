package q223.arrays;

/**
 *
 * @author cstan
 */
public class ArrayTest {

    /**
     * Which of the following options can be used in the code above so that the
     * init method initializes each table element to the sum of its row and
     * column number and the multiply method just multiplies the element value
     * by 2?
     */
    static int[][] table = new int[2][3];

    public static void init() {
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                //insert code to initialize     
                table[x][y] = x + y;
            }
        }
    }

    public static void multiply() {
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                //insert code to multiply   
                table[x][y] = table[x][y] * 2;
            }
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
