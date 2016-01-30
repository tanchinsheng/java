package q094.javadatatypes;

public class Widget {

    static int MAX;     //1
    static final String CLASS_GUID;   // 2

    static {
        MAX = 111;
        CLASS_GUID = "XYZ123";
    }

    Widget() {
        //3
    }

    Widget(int k) {
        //4
    }
}
