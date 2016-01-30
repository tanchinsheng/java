package q091.javadatatypes;

public class app {

    public static void main(String[] args) {
        short s = 12;
        long g = 012; // 012 is a valid octal number.
        System.out.println(g);
        int i = (int) false; // Values of type boolean cannot be converted to any other types.
        float f = -123; // Implicit widening conversion will occur in this case.
        // double cannot be implicitly narrowed to a float even though the value is representable by a float.
        float d1 = 0 * 1.5;
        float d2 = 0 * 1.5f;
        float d3 = 0 * (float) 1.5;
        float d4 = 0 * (byte) 1.5;
        float d5 = 0 * 30L;
        char d6 = 0 * 15L;

    }

}
