package q250.loop;

public class TestClass {

    public static void main(String args[]) {
        int sum = 0;
        for (int i = 0, j = 10; sum > 20; ++i, --j) // 1
        // Note that the loop condition is sum >20 and not sum <20.
        {
            sum = sum + i + j;
        }
        System.out.println("Sum = " + sum); // 0
    }

}
