package ocpjp.pretest._05;

/**
 * Hello world!
 *
 */
class NullInstanceof {

    public static void main(String[] args) {
        
        // When executed, this program will print the following: str is not Object.
        // The variable str was declared but not instantiated; 
        // hence the instanceof operator returns false.
        String str = null;
        if (str instanceof Object) // NULLCHK
        {
            System.out.println("str is Object");
        } else {
            System.out.println("str is not Object");
        }
    }
}