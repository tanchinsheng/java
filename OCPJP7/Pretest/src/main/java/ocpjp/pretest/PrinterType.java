package ocpjp.pretest;

public enum PrinterType {

    //Must be before member variable!
    DOTMATRIX(5), INKJET(10), LASER(50); // #2
    private int pagePrintCapacity; // #1

    //DOTMATRIX(5), INKJET(10), LASER(50); // #2
    private PrinterType(int pagePrintCapacity) {
        this.pagePrintCapacity = pagePrintCapacity;
    }

    public int getPrintPageCapacity() {
        return pagePrintCapacity;
    }

    public static void main(String[] args) {
    }
}