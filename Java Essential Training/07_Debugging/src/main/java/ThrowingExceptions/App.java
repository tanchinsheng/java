package ThrowingExceptions;

public class App {

    public static void main(String[] args) {

        try {
            getArrayItem();
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            System.err.println("Ouch!");
        }

    }

    private static void getArrayItem() throws ArrayIndexOutOfBoundsException {
        String[] strings = {"Welcome!"};
        System.out.println(strings[1]);
    }

}
