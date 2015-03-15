package ocpjp.pretest._37;

class Worker extends Thread {

    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

class Master {

    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setName("Master ");
        Thread worker = new Worker();
        worker.setName("Worker ");
        worker.start();
        //Calling the join()
        //method on itself means that the thread waits itself to complete, which would never happen
        Thread.currentThread().join();
        System.out.println(Thread.currentThread().getName());
    }
}
