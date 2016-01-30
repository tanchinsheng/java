package q432.inheritance;

class Super2 {

    static {
        System.out.print("Super ");
    }
}

class One {

    static {
        System.out.print("One ");
    }
}

class Two extends Super2 {

    static {
        System.out.print("Two ");
    }
}

public class app {

    public static void main(String[] args) {
        One o = null;
        Two t = new Two();
        System.out.println((Object) o == (Object) t);
    }

}
