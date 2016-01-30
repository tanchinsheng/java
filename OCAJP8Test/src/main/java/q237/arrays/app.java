package q237.arrays;

public class app {

    public static void main(String[] args) {
        String[] sA1 = new String[1] {"aaa"
        }; // Array size cannot be given here as the array is being initialized in the declaration.

        String[] sA2 = new String[]{"aaa"};

        String[] sA3 = new String[1];
        sA3[0] = "aaa";

        String[] sA4 = {new String("aaa")};

        String[] sA5 = {"aaa"};
    }

}
