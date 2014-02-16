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
        user.setAddress(address);
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
