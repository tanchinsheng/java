/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.lcom4;

/**
 *
 * @author cstan
 */
public class Address {

    public String street;
    public String city;
    public String zipCode;

    public String getFullAddress() {
        return street + " " + city + " " + zipCode;
    }
}
