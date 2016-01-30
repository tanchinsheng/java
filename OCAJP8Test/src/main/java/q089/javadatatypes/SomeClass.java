package q089.javadatatypes;

public class SomeClass {

    String s1 = "green mile"; // 0    
    String local; // 1

    public void generateReport(int n) {
        // String local; // 1
        if (n > 0) {
            local = "good";//2
        }
        /* compile
         else {
         local = "bad";
         }
         */
        System.out.println(s1 + " = " + local);//3
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
