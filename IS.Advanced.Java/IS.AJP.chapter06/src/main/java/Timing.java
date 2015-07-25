public class Timing {

   static void bubbleSort(int[] arr) {
      int size = arr.length;
      for (int pass = 1; pass < size; pass++) {
         for (int i = 0; i < size-pass; i++) {
            if (arr[i] > arr[i+1]) {
               int temp = arr[i];
               arr[i] = arr[i+1];
               arr[i+1] = temp;
            }
         }
      }
   }

   static void quickSort(int[] arr, int left, int right) {
      int i = left;
      int j = right;
      int temp;
      int pivot = arr[(left+right)/2];
      while (i <= j) {
         while (arr[i] < pivot) 
            i++;
         while (arr[j] > pivot)
            j--;
         if (i <= j) {
            temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
            j--;
         }
      }
      if (left < j)
         quickSort(arr, left, j);
      if (i < right)
         quickSort(arr, i, right);
   }

   public static void main(String[] args) {
      final int size = 1000;
      int[] numbers = new int[size];
      for (int i = 0; i < size; ++i) {
         numbers[i] = (int)(Math.random() * 100);
      }
      long startTime = System.nanoTime();
      bubbleSort(numbers);
      long stopTime = System.nanoTime();
      long duration = stopTime - startTime;
      System.out.println("Bubble sort took " + duration + " nanoseconds");
      startTime = System.nanoTime();
      quickSort(numbers, 0, size-1);
      stopTime = System.nanoTime();
      duration = stopTime - startTime;
      System.out.println("Quick sort took " + duration + " nanoseconds");
   }
}
