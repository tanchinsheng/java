package q163.operators.decision;

public class app {

    public static void main(String[] args) {
        String a = "java";
        char[] b = {'j', 'a', 'v', 'a'};
        String c = new String(b);
        String d = a;
        if (a == d) {
            System.out.println("a==d");
        }
        if (a == "java") {
            System.out.println("a==java");
        }
        if (a.equals(c)) {
            System.out.println("a.equals(c)");
        }
        //Note that a == c will be false because doing 'new' creates an entirely new object.
        if (b == d); // b and d can not even be compared because they are of different types.
    }
}
