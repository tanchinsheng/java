/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q519.exceptions;

/**
 *
 * What is the result of compiling and running this code?
 */
class MyException extends Throwable {
}

class MyException1 extends MyException {
}

class MyException2 extends MyException {
}

class MyException3 extends MyException2 {
}

public class ExceptionTest {

    /**
     * You can have multiple catch blocks to catch different kinds of
     * exceptions, including exceptions that are subclasses of other exceptions.
     * However, the catch clause for more specific exceptions (i.e. a
     * SubClassException) should come before the catch clause for more general
     * exceptions ( i.e. a SuperClassException). Failure to do so results in a
     * compiler error as the more specific exception is unreachable. In this
     * case, catch for MyException3 cannot follow catch for MyException because
     * if MyException3 is thrown, it will be caught by the catch clause for
     * MyException. And so, there is no way the catch clause for MyException3
     * can ever execute. And so it becomes an unreachable statement.
     */
    void myMethod() throws MyException {
        throw new MyException3();
    }

    public static void main(String[] args) {
        ExceptionTest et = new ExceptionTest();
        try {
            et.myMethod();
        } catch (MyException me) {
            System.out.println("MyException thrown");
        } catch (MyException3 me3) { // Fail to compile
            System.out.println("MyException3 thrown");
        } finally {
            System.out.println(" Done");
        }
    }

}
