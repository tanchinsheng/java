package q170.operators.decision;

public class app {

    public void ifTest(boolean flag) {
        if (flag) //1    
        {
            if (flag) //2    
            {
                System.out.println("True False");
            } else// 3
            {
                System.out.println("True True"); // It will never print 'True True'
            }
        } else// 4
        {
            System.out.println("False False");
        }

    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
