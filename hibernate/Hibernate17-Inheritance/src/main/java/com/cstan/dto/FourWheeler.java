/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cstan.dto;

import javax.persistence.Entity;

/**
 *
 * @author cstan
 */
@Entity
public class FourWheeler extends Vehicle{
    private String SteeringWheel;

    public String getSteeringWheel() {
        return SteeringWheel;
    }

    public void setSteeringWheel(String SteeringWheel) {
        this.SteeringWheel = SteeringWheel;
    }
    
}
