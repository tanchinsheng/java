package q061.javadatatypes;

public class TestClass {

    public static int getSwitch(String str) {
        return (int) Math.round(Double.parseDouble(str.substring(1, str.length() - 1)));
    }

    public static void main(String args[]) {

        System.out.println(Math.round(-0.5));
        System.out.println(args[0]);
        System.out.println((args[0].substring(1, args[0].length() - 1)));
        System.out.println(Double.parseDouble(args[0].substring(1, args[0].length() - 1)));

        switch (getSwitch(args[0])) {
            case 0:
                System.out.print("Hello ");
            case 1:
                System.out.print("World");
                break;
            default:
                System.out.print("Good Bye");
        }
    }
}
