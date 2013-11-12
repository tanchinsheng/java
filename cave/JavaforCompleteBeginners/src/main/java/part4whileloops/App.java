package part4whileloops;

/**
we look using loops to make your code repeat statements multiple times. 
* We also take a look at conditions.
 */
public class App {
  public static void main(String[] args) {
         
        int value = 0;   
        while(value < 10)
        {
            System.out.println("Hello " + value);        
            value = value + 1;
        }
    }
}
