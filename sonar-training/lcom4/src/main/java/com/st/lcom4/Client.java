/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.lcom4;

/**
 *
 * @author cstan
 */
public class Client {

    public String firstname;
    public String lastname;
    public Address address;

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public String getFullAddress() {
        return address.getFullAddress();
    }
}
