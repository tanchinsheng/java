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
//        for (int i = 0; i < 10; i++) {
//            UserDetails user = new UserDetails();
//            user.setUserName("User " + i);
//            session.persist(user);
//        }
        UserDetails user = (UserDetails) session.get(UserDetails.class, 5);
        user.setUserName("Updated User");
        session.update(user);
        //session.delete(user);
        session.getTransaction().commit();
        session.close();
        System.out.println("User name pulled up is: " + user.getUserName());
    }
}
