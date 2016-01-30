package q428.inheritance;

interface Automobile {

    String describe();
}

class FourWheeler implements Automobile {

    String name;

    public String describe() {
        return " 4 Wheeler " + name;
    }
}

class TwoWheeler extends FourWheeler {

    String name;

    public String describe() {
        return " 2 Wheeler " + name;
    }
}

public class app {

    public static void main(String[] args) {
    }

}
