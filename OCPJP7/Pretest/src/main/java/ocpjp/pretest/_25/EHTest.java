package ocpjp.pretest._25;

class InvalidValueException extends IllegalArgumentException {
}

class InvalidKeyException extends IllegalArgumentException {
}

class BaseClass {

    public String name = "BaseClass.name";

    void foo() throws IllegalArgumentException {
        System.out.println("BaseClass");
        throw new IllegalArgumentException();
    }
}

class DeriClass extends BaseClass {

    public String name = "DeriClass.name";

    public void foo() throws IllegalArgumentException {
        System.out.println("DeriClass");
        throw new InvalidValueException();
    }
}

class DeriDeriClass extends DeriClass {

    public String name = "DeriDeriClass.name";

    // It is not necessary to provide an Exception thrown by a method
    // when the method is overriding a method defined
    // with an exception (using the throws clause).
    public void foo() { // LINE A
        System.out.println("DeriDeriClass");
        throw new InvalidKeyException();
    }
}

class EHTest {

    public static void main(String[] args) {
        try {
            BaseClass base = new DeriClass();
            //DeriClass base = new DeriDeriClass();
            //BaseClass base = new DeriDeriClass();
            System.out.println(base.name);
            base.foo();
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
