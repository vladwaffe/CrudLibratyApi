package com.my.libraryservice.hibernate;

import com.my.libraryservice.model.LibraryBook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {

    private static final SessionFactory sessionFactory;
    private static Session session;

    static{
        Configuration cfg = null;
        try {
            cfg = new Configuration().configure();
            cfg.addAnnotatedClass(LibraryBook.class);
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }

        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        ServiceRegistry service=ssrb.build();
        sessionFactory = cfg.buildSessionFactory(service);
    }


    public static Session startSession(){
        return sessionFactory.openSession();
    }

    public static Session getSession(){
        return sessionFactory.getCurrentSession();
    }


    public static void closeSession(){
        if(session != null && session.isOpen()){
            session.close();
        }
    }

}
