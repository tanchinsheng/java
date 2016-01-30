package q095.javadatatypes;

public class app {

    public static void main(String[] args) {
        System.out.println(Boolean.parseBoolean("true"));
        System.out.println(new Boolean(null));
        // System.out.println(new Boolean());
        System.out.println(new Boolean("true"));
        System.out.println(new Boolean("trUE"));
        System.out.println(Boolean.valueOf("true"));
        System.out.println(new Boolean("true") == new Boolean("true"));
        System.out.println(new Boolean("true") == Boolean.parseBoolean("true"));
        System.out.println(new Boolean("true") == Boolean.valueOf("true"));
    }

}
