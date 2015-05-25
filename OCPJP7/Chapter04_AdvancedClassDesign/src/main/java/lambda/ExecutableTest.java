/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

interface Executable {

    int execute(int a, int b);
}

interface StringExecutable {

    int execute(String a);
}

class Runner {

    public void run(Executable e) {
        System.out.println("Executing code block...");
        int value = e.execute(12, 1);
        System.out.println("Return value is : " + value);
    }

//    public void run(StringExecutable e) {
//        System.out.println("Executing code block...");
//        int value = e.execute("Hello");
//        System.out.println("Return value is : " + value);
//    }
}

public class ExecutableTest {

    public static void main(String[] args) {
        // JDK 7
        // final int c = 100;
        int c = 100;
        // Musn't do this : c = 8;

        int d = 77;

        Runner runner = new Runner();
        runner.run(new Executable() {

            @Override
            public int execute(int a, int b) {
                System.out.println("Hello there ");
                // Cannot do in anonymousr classes : int d = 8;
                return (c + b + a);
            }

        });
        //System.out.println("=========================");
        //runner.run(() -> System.out.println("Hello there again"));
        System.out.println("=========================");
        runner.run((int a, int b) -> {
            System.out.println("This is code passed in a lambda expression ");
            System.out.println("Hello there again and again");
            return (c + a + b);
        });
        System.out.println("=========================");
        runner.run((a, b) -> {
            //int d = 99;// compilation  error!
            System.out.println("This is code passed in a lambda expression ");
            System.out.println("Hello there again and again");
            return (c + a + b);
        });
//        runner.run(a -> {
//            System.out.println("This is code passed in a lambda expression ");
//            System.out.println("Hello there again and again");
//            return 9 + a;
//        });
//        System.out.println("=========================");
//        runner.run((int a) -> (10 + a));

        System.out.println("=========================");
        Executable ex = (a, b) -> {
            //int d = 99;// compilation  error!
            System.out.println("This is code passed in a lambda expression ");
            System.out.println("Hello there again and again");
            return (c + a + b);
        };
        runner.run(ex);
        System.out.println("=========================");
        Object codeblock = (Executable) (a, b) -> {
            //int d = 99;// compilation  error!
            System.out.println("This is code passed in a lambda expression ");
            System.out.println("Hello there again and again");
            return (c + a + b);
        };
    }

}
