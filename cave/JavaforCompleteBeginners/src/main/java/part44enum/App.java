package part44enum;

/**
This is a tutorial on basic and advanced usage of the “enum” keyword in Java. 
* We’ll also take a look at how enum works behind the scenes, and what enum “constants” 
* actually are, as well as how to transform between enum constants and strings 
* (very useful for working with databases).
 */
public class App {
     
    public static void main(String[] args) {
         
        Animal animal = Animal.DOG;
         
        switch(animal) {
        case CAT:
            System.out.println("Cat");
            break;
        case DOG:
            System.out.println("Dog");
            break;
        case MOUSE:
            break;
        default:
            break;
        }
         
        System.out.println(Animal.DOG);
        //System.out.println("Enum name as a string: " + Animal.DOG.name());        
        //System.out.println(Animal.DOG.getClass());      
        //System.out.println(Animal.DOG instanceof Enum);      
        //System.out.println(Animal.MOUSE.getName());  
        Animal animal2 = Animal.valueOf("CAT");  
        System.out.println(animal2);
    }
 
}