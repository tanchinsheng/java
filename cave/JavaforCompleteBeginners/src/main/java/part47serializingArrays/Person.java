package part47serializingArrays;

/*
If you want to serialize multiple objects in Java, there are various easy ways to do it, 
including serializing the objects one by one, serializing an entire array in one go and 
serializing a whole Java collection. In this tutorial we’ll look at example of each of these, 
along with a trick or tip or two along the way.
*/

import java.io.Serializable;
 
public class Person implements Serializable {
     
    private static final long serialVersionUID = 4801633306273802062L;
     
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