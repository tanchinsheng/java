package Loop;

public class Loop {

    public static void main(String[] args) throws InterruptedException {

        long t1 = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 100000; j++) {
                //System.out.println("i = " + i + ",j= " + j);
            }
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
        t1 = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 100000; j++) {
                //System.out.println("i = " + i + ",j= " + j);
            }
        }
        t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
//        Execution time: 5379.003409999999 milliseconds
//Execution time: 5378.1379719999995 milliseconds
    }
}
