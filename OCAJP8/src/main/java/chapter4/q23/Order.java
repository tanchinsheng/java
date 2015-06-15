/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.q23;

public class Order {

    String value = "t";

    {
        value += "a";
    }

    {
        value += "c";
    }

    public Order() {
        value += "b";
    }

    public Order(String s) {
        value += s;
    }

    public static void main(String[] args) {
        Order order = new Order("f");
        order = new Order();
        System.out.println(order.value);
    }
}
