package com.mycompany;

public class CustomerDriver {

    public static void main(String[] args) {
        // Define a reference and creates a new Customer object
        Customer customer;
        customer = new Customer();
        //customer.balance = new BigDecimal(12506.45f);
        customer.setBalance(12506.45f);
        try {
            customer.setName(null);
        } catch (Exception e) {
            System.out.println("Names must not be null");
        }
        System.out.println(customer.toString());
    }
}
