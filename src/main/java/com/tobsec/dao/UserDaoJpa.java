package com.tobsec.dao;

import com.tobsec.model.User;
import java.util.List;

import javax.persistence.*;

import org.springframework.stereotype.Repository;

import com.tobsec.common.JpaTransaction;

@Repository("userDaoJpa")
public class UserDaoJpa implements UserDao {
    
    @PersistenceContext
    private EntityManager em;

    @JpaTransaction
    public int addUser(User user) {
        em.persist(user);
        em.flush();
        return 1;
    }

    @JpaTransaction
    public int updateUser(User user) {
        User userFind = em.find(User.class, user.getId());

        userFind.setName(user.getName());
        userFind.setEmail(user.getEmail());

        em.flush();

        return 1;
    }

    @JpaTransaction
    public int deleteUser(String id) {
        em.remove(em.find(User.class, id));

        return 1;
    }

    @JpaTransaction
    public int deleteAll() {
        return em.createQuery("Delete From User").executeUpdate();
    }

    public int countUserAll() {
        TypedQuery<Long> query = em.createQuery("Select Count(u) From User u", Long.class);

        return (int)(query.getSingleResult().longValue());
    }

    public int countUser(String id) {
        TypedQuery<Long> query = em.createQuery("Select Count(u) From User u Where u.id = :id", Long.class);
        query.setParameter("id", id);

        return (int)(query.getSingleResult().longValue());
    }

    public int countUserCondition(String option) {
        Query query = em.createNativeQuery("Select Count(*) From User " + option, Integer.class);

        return (Integer)query.getSingleResult();
    }

    public User getUser(String id) {
        return em.find(User.class, id);
    }

    public List<User> selectUserAll() {
        return em.createQuery("Select u From User u Order By u.id", User.class).getResultList();
    }

    public List<User> selectUserCondition(String option) {
        Query query = em.createNativeQuery("Select * From User " + option + " Order By id", User.class);

        return query.getResultList();
    }

    public void upgradeLevel(User user) {
        em.createQuery("Update User u u.level = :level Where u.id = :id")
          .setParameter("level", user.getLevel())
          .setParameter("id", user.getId())
          .executeUpdate();
    }
    
    public void plusLogin(User user, int login) {
        em.createQuery("Update User u Set u.login = u.login + :login Where u.id = :id")
          .setParameter("login", login)
          .setParameter("id", user.getId())
          .executeUpdate();
    }

    public void plusRecommend(User target, int recommend) {
        em.createQuery("Update User u Set u.recommend = u.recommend + :recommend Where u.id = :id")
          .setParameter("recommend", recommend)
          .setParameter("id", target.getId())
          .executeUpdate();
    }

    public void checkedRecommend(User user) {
        em.createQuery("Update User u Set u.recid = :recid Where u.id = :id")
          .setParameter("recid", user.getRecid())
          .setParameter("id", user.getId())
          .executeUpdate();
    }
}