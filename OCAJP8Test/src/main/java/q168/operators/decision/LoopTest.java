package q168.operators.decision;

public class LoopTest {

    int k = 5;

    public boolean checkIt(int k) {
        return k-- > 0 ? true : false;
    }

    public void printThem() {
        while (checkIt(k)) {
            System.out.print(k);
            //System.out.print(k--); // 54321
        }
    }

    public static void main(String[] args) {
        new LoopTest().printThem();
    }

}
