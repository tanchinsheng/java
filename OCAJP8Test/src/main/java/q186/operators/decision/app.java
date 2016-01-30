package q186.operators.decision;

public class app {

    public static void main(String[] args) {
        byte b = 1;  // this is integer
        char c = 1;
        short s = 1;
        int i = 1;

        s = b * b; // b * b returns an int.
        i = b + b;
        s *= b; // All compound assignment operators internally do an explicit cast.
        c = c + b; // c + b returns an int
        s += i; // All compound assignment operators internally do an explicit cast.
    }

}
