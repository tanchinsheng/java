package q189.operators.decision;

public class app {

    public static void ifTest(boolean flag) {
        if (flag) //1
        {
            if (flag) //2
            {
                if (flag) //3
                {
                    System.out.println("False True");
                } else //4
                {
                    System.out.println("True False");
                }
            } else //5
            {
                System.out.println("True True");
            }
        } else //6
        {
            System.out.println("False False");
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
