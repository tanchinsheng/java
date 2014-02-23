package com.cstan.hibernate;

import com.cstan.dto.UserDetails;
import java.util.List;
import org.hibernate.Query;
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
        user.setUserName("User 1");
        UserDetails user2 = new UserDetails();
        user2.setUserName("User 2");
        session.save(user);
        session.save(user2);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from UserDetails where userId > 1");
        List users = query.list();
        session.getTransaction().commit();
        session.close();
        System.out.println("Size of list result= " + users.size());

    }
}
