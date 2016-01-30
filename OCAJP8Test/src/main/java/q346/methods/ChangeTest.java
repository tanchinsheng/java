package q346.methods;

public class ChangeTest {

    private int myValue = 0;

    public void showOne(int myValue) {
        myValue = myValue;
        System.out.println(this.myValue);
    }

    public void showTwo(int myValue) {
        this.myValue = myValue;
        System.out.println(this.myValue);
    }

    public static void main(String[] args) {
        ChangeTest ct = new ChangeTest();
        ct.showOne(100);
        ct.showTwo(200);
    }

}
