package com.cstan.dto;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int vehicleId;
    private String vehicleName;
    @ManyToOne
    @NotFound(action=NotFoundAction.IGNORE)
    private UserDetails user;

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

}
