package com.cstan.hibernate;

import com.cstan.dto.Address;
import com.cstan.dto.UserDetails;
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

        Address address = new Address();
        address.setCity("Singapore");
        address.setPincode("730587");
        address.setState("State");
        address.setStreet("Woodlands Dr 16");
        user.setHomeAddress(address);

        Address address2 = new Address();
        address2.setCity("Second City Name");
        address2.setPincode("Second 730587");
        address2.setState("Second State");
        address2.setStreet("Second Street Name");
        user.setOfficeAddress(address2);

        Configuration cfg = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory sessionFactory = cfg.buildSessionFactory(builder.build());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);

        session.getTransaction().commit();
        session.close();

    }
}
