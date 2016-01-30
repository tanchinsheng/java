package q245.arrays;

public class StringArrayTest {

    public static void main(String[] args) {
        String[][][] arr = {{{"a", "b", "c"},
        {"d", "e", null}},
        {{"x"}, null}, {{"y"}},
        {{"z", "p"}, {}}};
        System.out.println(arr[0][1][2]);
    }

}
