package listing602;

/*------------------------------------------------------------------------------
 * Oracle Certified Professional Java SE 7 Programmer Exams 1Z0-804 and 1Z0-805:
 * A Comprehensive OCPJP 7 Certification Guide
 * by SG Ganesh and Tushar Sharma
 ------------------------------------------------------------------------------*/
class BoxPrinterTestOne {

    public static void main(String[] args) {
        BoxPrinter1 value1 = new BoxPrinter1(new Integer(10));
        System.out.println(value1);
        BoxPrinter1 value2 = new BoxPrinter1("Hello world");
        System.out.println(value2);
    }
}
