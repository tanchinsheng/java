package Loop;

public class ReverseLoop {

    public static void main(String[] args) throws InterruptedException {

        long t1 = System.nanoTime();
        for (int i = 100000 - 1; i >= 0; i--) {
            for (int j = 100000 - 1; j >= 0; j--) {
                //System.out.println("i = " + i + ",j= " + j);
            }
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
        t1 = System.nanoTime();
        for (int i = 100000 - 1; i >= 0; i--) {
            for (int j = 100000 - 1; j >= 0; j--) {
                //System.out.println("i = " + i + ",j= " + j);
            }
        }
        t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
//Execution time: 5394.296862 milliseconds
//Execution time: 5445.729635 milliseconds
    }
}
