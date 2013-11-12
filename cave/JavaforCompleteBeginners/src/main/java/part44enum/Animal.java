package part44enum;

public enum Animal {
    CAT("Fergus"), DOG("Fido"), MOUSE("Jerry");
     
    private final String name;
     
    Animal(String name) {
        this.name = name;
    }
 
    public String getName() {
        return name;
    }
     
    @Override
    public String toString() {
        return "This animal is called: " + name;
    }
}