package com.cstan.hibernate;

import com.cstan.dto.UserDetails;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

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

        Criteria criteria = session.createCriteria(UserDetails.class);
       // criteria.add(Restrictions.eq("userName", "User 3"))
        //       .add(Restrictions.gt("userId", 2));
        criteria.add(Restrictions.or(Restrictions.between("userId", 0, 1), Restrictions.between("userId", 2, 3)));
        List<UserDetails> users = (List<UserDetails>) criteria.list();
        session.getTransaction().commit();
        session.close();
        for (UserDetails u : users) {
            System.out.println(u.getUserName());
        }

    }
}
