package q187.operators.decision;

public class Test {

    public static void main(String[] args) {
        int d = 0;
        try {
            int i = 1 / (d * doIt());
        } catch (Exception e) {
            System.out.println(e);
            // java.lang.Exception: Forget It
        }
    }

    public static int doIt() throws Exception {
        throw new Exception("Forget It");
    }

}
