/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.lcom4;

/**
 *
 * @author cstan
 */
public class BadClient {

    public String firstname;
    public String lastname;
    public String street;
    public String city;
    public String zipCode;

    public BadClient() {
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public String getFullAddress() {
        return this.street + " " + this.city + " " + this.zipCode;
    }
}
