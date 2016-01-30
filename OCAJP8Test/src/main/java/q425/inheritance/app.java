package q425.inheritance;

interface I1 {

    void setValue(String s);

    String getValue();
}

interface I2 extends I1 {

    void analyse();
}

abstract class B implements I1 {

    int getValue(int i) {
        return 0;
    }
}

class A extends I1 { //Classes do not extend interfaces, they implement interfaces.

    String s;

    void setValue(String val) {
        s = val;
    }

    String getValue() {
        return s;
    }
}

interface I3

    implements I1{
    void perform_work();
} //Interfaces do not implement anything, they can extend multiple interfaces.

public class app {

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
