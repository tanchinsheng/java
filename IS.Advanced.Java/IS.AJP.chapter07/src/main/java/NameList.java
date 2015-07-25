public class NameList {
   public static void main(String[] args) {
      try {
         List<String> names = new List<String>(10);
         System.out.println(names.toString());
      }
      catch (ListEmptyException e) {
         e.printStackTrace();
      }
   }
}
      