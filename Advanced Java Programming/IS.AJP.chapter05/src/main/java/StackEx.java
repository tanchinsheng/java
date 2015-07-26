
import java.util.Stack;

/**
 *
 * @author cstan
 */
public class StackEx {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Stack<String> names = new Stack<>();
        names.push("Raymond");
        names.push("David");
        System.out.println("Top of stack: " + names.peek());
        names.pop();
        System.out.println("Top of stack: " + names.peek());
        names.push("Cynthia");
        System.out.println("Top of stack: " + names.peek());
        if (!names.empty()) {
            names.pop();
        }
        System.out.println("Top of stack: " + names.peek());
        names.pop();
        if (!names.empty()) {
            System.out.println("Top of stack: " + names.peek());
        } else {
            System.out.println("Stack emtpy");
        }

    }

}
