package q335.methods.b;

import q335.methods.a.AccessTest;

public class AccessTester extends AccessTest {

    public static void main(String[] args) {
        AccessTest ref1 = new AccessTest();
        ref1.d();
        AccessTester ref2 = new AccessTester();
        ref2.c();
        ref2.d();
        AccessTest ref3 = new AccessTester();
        ref3.d();
    }

}
