/*
set of statements;
try 
{
   set of statements that might throw exceptions
}
catch (Exception object) 
{
   set of statements to clean up 
}
*/
public class TryCatch {
   public static void main(String[] args) {
      int numer = 12;
      int denom = 0;
      try {
         System.out.print(numer / denom);
      }
      catch (ArithmeticException e) {
         System.out.println("Can't divide by zero.");
         denom = 2;
         System.out.print(numer / denom);
      }
      // program continues on
   }
}