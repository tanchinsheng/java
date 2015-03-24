package listing604;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
// This program shows container implementation using generics
class BoxPrinter3<T> {

    private T val;

    public BoxPrinter3(T arg) {
        val = arg;
    }

    public String toString() {
        return "[" + val + "]";
    }
}
