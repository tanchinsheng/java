package q100.javadatatypes;

public class Test {

    public static void main(String[] args) {
        int i = 10;
        byte b = 20;
        b = i;//will not compile because byte is smaller than int
        b = (byte) i; //OK

        final int k = 10;
        b = k; //Okay because k is final and 10 fits into a byte

        final float f = 10.0;//will not compile because 10.0 is a double even though the value 10.0 fits into a float
        i = f;//will not compile.
    }

}
