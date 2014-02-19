/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cstan.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Bike")
public class TwoWheeler extends Vehicle{
    private String SteeringHandle;

    public String getSteeringHandle() {
        return SteeringHandle;
    }

    public void setSteeringHandle(String SteeringHandle) {
        this.SteeringHandle = SteeringHandle;
    }
    
}
