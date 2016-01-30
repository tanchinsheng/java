package q275.loop;

public class app {

    public void method1(int i) {
        int j = (i * 30 - 2) / 100;

        POINT1:
        for (; j < 10; j++) {
            boolean flag = false;
            while (!flag) {
                if (Math.random() > 0.5) {
                    break POINT1;
                }
            }
        }
        while (j > 0) {
            System.out.println(j--);
            if (j == 4) {
                break POINT1;
            }
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
