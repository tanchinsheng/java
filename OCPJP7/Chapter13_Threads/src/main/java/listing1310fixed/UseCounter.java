package listing1310fixed;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
class UseCounter implements Runnable {

    // added static
    public static void increment() {
        synchronized (UseCounter.class) {
            Counter.count++;
            System.out.print(Counter.count + "  ");
        }
    }

    @Override
    public void run() {
        increment();
        increment();
        increment();
    }
}
