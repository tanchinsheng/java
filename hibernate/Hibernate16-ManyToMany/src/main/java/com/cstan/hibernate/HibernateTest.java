package com.cstan.hibernate;

import com.cstan.dto.UserDetails;
import com.cstan.dto.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateTest {

    private HibernateTest() {
    }

    public static void main(String[] args) {

        UserDetails user = new UserDetails();
        user.setUserName("First User");
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleName("Car");
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleName("Jeep");

        user.getVehicle().add(vehicle);
        user.getVehicle().add(vehicle2);

        vehicle.getUserList().add(user);
        vehicle2.getUserList().add(user);

        Configuration cfg = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory sessionFactory = cfg.buildSessionFactory(builder.build());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.save(vehicle);
        session.save(vehicle2);
        session.getTransaction().commit();
        session.close();

    }
}
