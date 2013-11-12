package part46serialization;
import java.io.Serializable;

//public class Person {
public class Person implements Serializable {
    
    private final int id;
    private final String name;
     
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + "]";
    }
}