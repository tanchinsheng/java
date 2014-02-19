package com.cstan.hibernate;

import com.cstan.dto.FourWheeler;
import com.cstan.dto.TwoWheeler;
import com.cstan.dto.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateTest {

    private HibernateTest() {
    }

    public static void main(String[] args) {
        
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleName("Car");
        
        TwoWheeler bike = new TwoWheeler();
        bike.setVehicleName("Bike");
        bike.setSteeringHandle("Bike Steering Handler");
        
        FourWheeler car = new FourWheeler();
        car.setVehicleName("Porsche");
        car.setSteeringWheel("Porsche Steering wheel");

        Configuration cfg = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory sessionFactory = cfg.buildSessionFactory(builder.build());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(vehicle);
        session.save(bike);
        session.save(car);
        session.getTransaction().commit();
        session.close();

    }
}
