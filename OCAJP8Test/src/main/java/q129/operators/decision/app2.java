package q129.operators.decision;

public class app2 {

    public static void main(String args[]) {
        System.out.println(args.length);
        try {
            System.out.println(args[args.length - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

}
