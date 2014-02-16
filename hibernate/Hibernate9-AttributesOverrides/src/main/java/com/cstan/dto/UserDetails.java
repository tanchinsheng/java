/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cstan.dto;

import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author tachinse
 */
@Entity
// for HQL
@Table(name = "USER_DETAILS")
public class UserDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String userName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street",
                column = @Column(name = "HOME_STREET_NAME")),
        @AttributeOverride(name = "city",
                column = @Column(name = "HOME_CITY_NAME")),
        @AttributeOverride(name = "state",
                column = @Column(name = "HOME_STATE_NAME")),
        @AttributeOverride(name = "pincode",
                column = @Column(name = "HOME_PINCODE_NAME")),})
    private Address homeAddress;

    @Embedded
    private Address officeAddress;

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(Address officeAddress) {
        this.officeAddress = officeAddress;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
