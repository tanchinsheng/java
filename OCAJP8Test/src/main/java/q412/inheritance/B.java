package q412.inheritance;

class A {

    public static void sM1() {
        System.out.println("In base static");
    }
}

class B extends A {

    public static void sM1() {
        System.out.println("In sub static");
    } // line1

    public void sM1() {
        System.out.println("In sub non-static");
    } // line2
}
