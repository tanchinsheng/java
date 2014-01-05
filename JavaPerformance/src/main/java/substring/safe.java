package substring;

public class safe{
   public static void main(String args[]){
      String Str = "Welcome to Tutorialspoint.com";

      System.out.print("Return Value :" );
      System.out.println(Str.substring(10) );

      System.out.print("Return Value :" );
      System.out.println(Str.substring(10, 150) );
   }
}