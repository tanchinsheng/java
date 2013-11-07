package demo1;

/**
 * The causes of deadlock and two things you can do about it. This video also
 * covers how to write a method that can safely acquire any number of locks in
 * any order without causing deadlock, using the tryLock() method of
 * ReentrantLock.
 */
public class App {

    public static void main(String[] args) throws Exception {

        final Runner runner = new Runner();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    runner.firstThread();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    runner.secondThread();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        runner.finished();
    }

}
