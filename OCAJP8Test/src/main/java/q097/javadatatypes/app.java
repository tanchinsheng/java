package q097.javadatatypes;

public class app {

    public static void main(String[] args) {
        Boolean.parseBoolean(" true ");
        // Although this will return true but it is still not a valid answer because
        // parseBoolean returns a primitive and not a Boolean wrapper object.
        /*
         Boolean.parseBoolean("true");
         public static Boolean valueOf(boolean b) {
         return (b ? TRUE : FALSE);
         }
         */

        Boolean.valueOf(true);
        Boolean.valueOf("trUE");
        Boolean bool = Boolean.TRUE;
    }

}
