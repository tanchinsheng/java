package q215.arrays;

public class app {

    public static void main(String[] args) {
        char[][] cA1 = {{'a', 'b', 'c'}, {'a', 'b', 'c'}};//1

        char cA2[][] = new char[3][];//2
        for (int i = 0; i < cA2.length; i++) {
            cA2[i] = new char[4];
        }

        char cA3[][] = {//3
            new char[]{'a', 'b', 'c'},
            new char[]{'a', 'b', 'c'}
        };

//        char cA4[3][2] = new char[][] {//4
//            {'a', 'b', 'c' },
//            {'a', 'b', 'c' }
//        };
        char[][] cA5 = {"1234", "1234", "1234"};//5
    }

}
