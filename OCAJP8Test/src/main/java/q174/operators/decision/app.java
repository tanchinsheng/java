package q174.operators.decision;

public class app {

    public static void main(String[] args) {
        int i = 0;
        String s = "";

        //s = null;
        if ((s != null) | (i == s.length())) {
        } //(i == str.length()) will always be executed so if 'str' is null, then str.length() will throw a NullPointerException.
        System.out.println("A");

        //s = null;       
        if ((s == null) | (i == s.length())) {
        } // (i == str.length()) will always be executed so if 'str' is null, then str.length() will throw a NullPointerException.
        System.out.println("B");

        //s = null;       
        if ((s != null) || (i == s.length())) {
        } // (i == str.length()) will only be evaluated if (str != null) is false, and (str != null) will be false if 'str' is null.
        //So it will also throw a NullPointerException.
        System.out.println("C");
        s = null;
        if ((s == null) || (i == s.length())) {
        } // (i == str.length()) will only be evaluated if (str == null) is false, and (str == null) will be false if 'str' is NOT null.
        // So it will NEVER throw a NullPointerException.
        System.out.println("D");
    }

}
