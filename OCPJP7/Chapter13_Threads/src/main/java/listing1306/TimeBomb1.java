package listing1306;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
class TimeBomb1 extends Thread {

    String[] timeStr = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};

    @Override
    public void run() {
        for (int i = 9; i >= 0; i--) {
            try {
                System.out.println(timeStr[i]);
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static void main(String[] s) {
        TimeBomb1 timer = new TimeBomb1();
        System.out.println("Starting 10 second count down... ");
        timer.start();
        try {
            timer.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Boom!!!");
    }
}
