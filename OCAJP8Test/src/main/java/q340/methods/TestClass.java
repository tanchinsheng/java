package q340.methods;

class Data {

    private int x = 0, y = 0;// now is private

    public Data(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setValues(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class TestClass {

    public static void main(String[] args) {
        Data d = new Data(1, 1);
        //add code here
        d.setValues(2, 2);
    }

}
