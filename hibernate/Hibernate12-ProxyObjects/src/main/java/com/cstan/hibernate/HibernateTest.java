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

        Address address1 = new Address();
        address1.setCity("Singapore");
        address1.setPincode("730587");
        address1.setState("State");
        address1.setStreet("Woodlands Dr 16");

        Address address2 = new Address();
        address2.setCity("Second City Name");
        address2.setPincode("Second 730587");
        address2.setState("Second State");
        address2.setStreet("Second Street Name");

        user.getListOfAddresses().add(address1);
        user.getListOfAddresses().add(address2);

        Configuration cfg = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory sessionFactory = cfg.buildSessionFactory(builder.build());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);

        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        user = (UserDetails) session.get(UserDetails.class, 1);
        session.close();
        System.out.println(user.getListOfAddresses().size());

    }
}
