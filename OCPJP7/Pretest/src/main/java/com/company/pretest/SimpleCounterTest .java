package com.company.pretest;

class SimpleCounter<T> {

    private static int count = 0;

    public SimpleCounter() {
        count++;
    }

    static int getCount() {
        return count;
    }
}

class SimpleCounterTest {

    public static void main(String[] args) {
        SimpleCounter<Double> doubleCounter = new SimpleCounter<Double>();
        SimpleCounter<Integer> intCounter = null;
        SimpleCounter rawCounter = new SimpleCounter(); // RAW
        System.out.println("SimpleCounter<Double> counter is "
                + doubleCounter.getCount());
        System.out.println("SimpleCounter<Integer> counter is " + intCounter.getCount());
        System.out.println("SimpleCounter counter is " + rawCounter.getCount());
    }
}