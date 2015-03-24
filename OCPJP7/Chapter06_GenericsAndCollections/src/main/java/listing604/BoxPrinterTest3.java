package listing604;

class BoxPrinterTest3 {

    public static void main(String[] args) {
        BoxPrinter3<Integer> value1 = new BoxPrinter3<>(new Integer(10));
        System.out.println(value1);

        BoxPrinter3<String> value2 = new BoxPrinter3<>("Hello world");
        System.out.println(value2);
    }
}
