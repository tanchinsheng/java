package booleans;

public class Main {

    public static void main(String[] args) {
        boolean b1 = true;
        boolean b2 = false;

        System.out.println(b1 + " " + b2);
        System.out.println(!b1);

        int i = 1;
        boolean b4 = (i != 0);
        System.out.println(b4);

        String s = "True";
        boolean b5 = Boolean.parseBoolean(s);
        System.out.println(b5);

        String s1 = "?";
        boolean b6 = Boolean.parseBoolean(s1);
        System.out.println(b6);

    }

}
