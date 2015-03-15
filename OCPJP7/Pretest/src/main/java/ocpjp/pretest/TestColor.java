package ocpjp.pretest;

class ShapeTest {

    public ShapeTest() {
        System.out.println("Shape constructor");
    }

    public class Color {    

        public Color() {
            System.out.println("Color constructor");
        }
    }
}

class TestColor {

    public static void main(String[] args) {
        //ShapeTest.Color black = new ShapeTest().Color(); // #1  
        ShapeTest.Color black = new ShapeTest(). new Color(); // #1

    }
}