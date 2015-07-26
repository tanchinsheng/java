public class List<T> {
   
   private T[] datastore;
   private int size;
   private int pos;

   public List(int numElements) {
      size = numElements;
      pos = 0;
      datastore = (T[]) new Object[size];
   }

   public void add(T element) {
      datastore[pos] = element;
      ++pos;
   }

   public String toString() throws ListEmptyException {
      if (datastore[0] == null) {
         throw new ListEmptyException();
      }
      String elements = "";
      for(int i = 0; i < pos; ++i) {
         elements += datastore[i] + " ";
      }
      return elements;
   }
}