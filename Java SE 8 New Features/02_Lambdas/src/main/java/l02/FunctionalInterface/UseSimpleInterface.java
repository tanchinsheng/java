package l02.FunctionalInterface;

import l02.FunctionalInterface.interfaces.SimpleInterface;

/**
 *
 * @author cstan
 */
public class UseSimpleInterface {

    public static void main(String[] args) {
        SimpleInterface obj = () -> System.out.println("Say something");
        obj.doSomething();
    }
}
