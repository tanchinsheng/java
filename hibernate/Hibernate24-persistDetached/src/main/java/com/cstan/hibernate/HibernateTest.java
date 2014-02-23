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

        Configuration cfg = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory sessionFactory = cfg.buildSessionFactory(builder.build());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        UserDetails user = new UserDetails();
        user.setUserName("First User");
        session.save(user);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        user = (UserDetails) session.get(UserDetails.class, 1);
        System.out.println(user.getUserName());
        session.getTransaction().commit();
        session.close();

        user.setUserName("Updated Username after session close");
        System.out.println(user.getUserName());
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(user);
        user.setUserName("Updated Username after update");
        System.out.println(user.getUserName());
        session.getTransaction().commit();
        session.close();
    }
}
