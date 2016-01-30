package q119.javadatatypes.gc;

public class TestClass {

    public static void main(String args[]) {
        Student s = new Student("Vaishali", "930012");
        s.grade();
        System.out.println(s.getName());

        s = null;
        s = new Student("Vaishali", "930012");
        s.grade();
        System.out.println(s.getName());
        s = null;
    }

}
