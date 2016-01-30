package q107.javadatatypes;

public class Noobs {

    public Noobs() {
        try {
            throw new MyException();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        Noobs a = new Noobs();
        Noobs b = new Noobs();
        Noobs c = a;
    }

}
