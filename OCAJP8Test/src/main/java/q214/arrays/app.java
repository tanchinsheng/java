package q214.arrays;

public class app {

    public static void main(String[] args) {
        //String[] sa1 = new String[3]{"a", "b", "c"};
        // You cannot specify the length of the array ( i.e. 3, here) if you are using the initializer block while declaring the array.
        String sa2[] = {"a ", " b", "c"};
        //String sa3 = new String[]{"a", "b", "c"};
        // here sa is not declared as array of strings but just as a String.
        String sa4[] = new String[]{"a", "b", "c"};
        //String sa5[] = new String[]{"a" "b" "c"};
        //There are no commas separating the strings.
    }

}
