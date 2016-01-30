package q114.javadatatypes;

public class Conversion {

    public static void main(String[] args) {
        int i = 1234567890;
        float f = i;
        System.out.println(f);
        System.out.println((int) f);
        System.out.println(i - (int) f);//-46

        int y = 32768;
        short x = (short) y;
        System.out.println(x);//722
    }

}
