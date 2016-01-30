package q442.inheritance;

class Doll {

    String name;

    Doll(String nm) {
        this.name = nm;
    }
}

class Barbie extends Doll {

    Barbie() {
        //1
        //super("unknown");
        this("unknown");
    }

    Barbie(String nm) {
        //2
        super(nm);

    }
}

public class TestClass {

    public static void main(String[] args) {
        Barbie b = new Barbie("mydoll");
    }

}
