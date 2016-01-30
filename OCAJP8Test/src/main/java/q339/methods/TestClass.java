package q339.methods;

class Data {

    int x = 0, y = 0;

    public Data(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class TestClass {

    public static void main(String[] args) {
        Data d = new Data(1, 1);
        //add code here
        d = new Data(2, 2); // This will create a new Data object and will not change the original Data object referred to be d.
        d.x = 2;//Correct
        d.y = 2;
        d.x += 1;//Correct
        d.y += 1;
        d = d + 1;//This will not compile because Java does not allow operator overloading for user defined objects.
    }

}
