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

        UserDetails userX = (UserDetails) session.get(UserDetails.class, 1);
        //userX.setUserName("Updated User 1");
        System.out.println(userX.getUserName());
        UserDetails userY = (UserDetails) session.get(UserDetails.class, 1);
        System.out.println(userY.getUserName());
        session.getTransaction().commit();
        session.close();

        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        UserDetails userZ = (UserDetails) session2.get(UserDetails.class, 1);
        session2.getTransaction().commit();
        session2.close();

    }
}
