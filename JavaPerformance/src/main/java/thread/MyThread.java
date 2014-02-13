package thread;

public class MyThread {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //doWork();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            });
            myThread.start();
            try {
                myThread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}