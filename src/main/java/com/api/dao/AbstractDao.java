package com.api.dao;

import com.api.util.HibernateUtil;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractDao<T> {
    
    protected Class<T> clazz;
    protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz(){
        return this.clazz;
    }
    
    public void setClazz(Class<T> clazz){
        this.clazz = clazz;
    }
    
    public List<T> findAll() {
        List<T> entities = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query querys = session.createQuery("from " + this.clazz.getSimpleName() + " e");
            entities = querys.list();
            session.getTransaction().commit();
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return entities;
    }

    public T get(String cid){
        T entity = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query querys = session.createQuery("from " + this.clazz.getSimpleName() + " e where e.carid = :ID");
            querys.setParameter("ID", cid);
            entity = (T) querys.uniqueResult();
            Hibernate.initialize(entity);
            session.getTransaction().commit();
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return entity;
    }
    
    public boolean create(T entity) {
        Boolean success = false;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            success = true;
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return success;
    }
    
    public boolean update(T entity) {
        Boolean success = false;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            success = true;
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return success;
    }
    
    public boolean delete(T entity) {
        Boolean success = false;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
            success = true;
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return success;
    }

}
