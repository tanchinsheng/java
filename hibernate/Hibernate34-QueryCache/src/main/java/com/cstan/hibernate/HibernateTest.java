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
        UserDetails user1 = new UserDetails();
        user1.setUserName("User 1");
        UserDetails user2 = new UserDetails();
        user2.setUserName("User 2");
        UserDetails user3 = new UserDetails();
        user3.setUserName("User 3");
        UserDetails user4 = new UserDetails();
        user4.setUserName("User 4");
        UserDetails user5 = new UserDetails();
        user5.setUserName("User 5");
        session.save(user1);
        session.save(user2);
        session.save(user3);
        session.save(user4);
        session.save(user5);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from UserDetails user where user.userId = 1");
        query.setCacheable(true);
        List users = query.list();
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        Query query2 = session2.createQuery("from UserDetails user where user.userId = 1");
        query2.setCacheable(true);
        users = query2.list();
        session2.getTransaction().commit();
        session2.close();

    }
}
