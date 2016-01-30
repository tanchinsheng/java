package q151.operators.decision;

public class app {

    public static void main(String[] args) {
        // operator + is left associative so evaluation of (1 + 2 + "3" ) is
        // as follows: ( 1 + 2 ) + "3" -> 3 + "3" -> "33".
        System.out.println(1 + 2 + "3");// would print 33
        // evaluation of ("1" + 2 + 3) is as follows: ("1" + 2) + 3 ->
        // "12" + 3 -> "123".
        System.out.println("1" + 2 + 3);// would print 15.
        // (4 + 1.0f ) evaluates as 4.0f + 1.0f ->5.0f -> 5.0
        System.out.println(4 + 1.0f); //would print 5.0
        //(5/4) performs integer division because both 5 and 4 are integers,
        // resulting in the value 1.
        System.out.println(5 / 4); //would print 1.25
        // Both operands in the expression ( 'a' + 1 ) will be promoted to
        // int => 97 + 1 = 98
        System.out.println('a' + 1); //would print b.
    }

}
