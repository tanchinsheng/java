package com.cstan.hibernate;

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
        user.setUserName("Test User");//transient

        Configuration cfg = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory sessionFactory = cfg.buildSessionFactory(builder.build());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //user.setUserName("Updated User");
        session.save(user);
        user.setUserName("Updated User");//persisted
        user.setUserName("Updated User Again");
        session.getTransaction().commit();
        session.close();
        user.setUserName("Updated User After Session Close");//detached
    }
}
