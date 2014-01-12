package com.company.pretest;

class EHBehaviour {

    public static void main(String[] args) {
        try {
            int i = 10 / 0; // LINE A
            System.out.print("after throw -> ");
        } catch (ArithmeticException ae) {
            System.out.print("in catch -> ");
            return;
        } finally {
            System.out.print("in finally -> ");
        }
        System.out.print("after everything");
    }
}