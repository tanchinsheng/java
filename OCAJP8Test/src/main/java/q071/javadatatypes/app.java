package q071.javadatatypes;

public class app {

    public static void main(String[] args) {
        // An underscore can only occur in between two digits. So the _ before L is invalid.
        long y = 123_456_L;
        // An underscore can only occur in between two digits.
        // So the _ before 1 is invalid. _123_456L is a valid variable name though.
        // So the following code is valid: int _123_456L = 10; long z = _123_456L;
        long z = _123_456L;
        // An underscore can only occur in between two digits. So the _ before . is invalid.
        float f1 = 123_.345_667F;
        float f2 = 123_345_667F;
    }

}
