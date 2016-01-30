package q195.operators.decision;

public class app {

    public static boolean getBool() {
        return true;
    }

    public static String getString() {
        return "true";
    }

    public static void main(String args[]) {
        //A boolean cannot be used in a case statement. So, as it is, the given code will not compile.
        //switch (getBool()) {
        switch (getString()) {
            //case true:
            case "true":
                System.out.println("true");
                break;
            default:
                System.out.println("none");
                break;
        }
    }
}
