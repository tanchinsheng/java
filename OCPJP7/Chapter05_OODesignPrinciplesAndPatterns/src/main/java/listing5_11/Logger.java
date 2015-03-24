package listing5_11;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
public class Logger {

    private Logger() {
        // private constructor
    }
    private static Logger myInstance;

    private static class LoggerHolder {

        private static Logger LOGGER = new Logger();
    }

    public static Logger getInstance() {
        return LoggerHolder.LOGGER;
    }

    public void log(String s) {
        // log implementation
        System.err.println(s);
    }
}
