package ocpjp.pretest;

class OverloadedColor {

    int red, green, blue;

    OverloadedColor() {
        // The compiler looks for the method Color() when it reaches this statement: 
        // Color(10, 10, 10);.
        //OverloadedColor(10, 10, 10);
        this(10, 10, 10);
    }

    OverloadedColor(int r, int g, int b) {
        red = r;
        green = g;
        blue = b;
    }

    void printColor() {
        System.out.println("red: " + red + " green: " + green + " blue: "
                + blue);
    }

    public static void main(String[] args) {
        OverloadedColor color = new OverloadedColor();
        color.printColor();
    }
}