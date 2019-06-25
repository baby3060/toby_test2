package com.tobsec.dao;

import com.tobsec.model.User;
import java.util.List;

import javax.persistence.*;

import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tobsec.common.JpaTransaction;

@Repository("userDao")
public class UserDaoJpa implements UserDao {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());

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
        Query query = em.createNativeQuery("Select Count(*) From User Where 1 = 1 " + option);

        return ((Number) query.getSingleResult()).intValue();
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

    @JpaTransaction
    public void upgradeLevel(User user) {
        em.createQuery("Update User u u.level = :level Where u.id = :id")
          .setParameter("level", user.getLevel())
          .setParameter("id", user.getId())
          .executeUpdate();
          em.flush();
    }
    
    @JpaTransaction
    public void plusLogin(User user, int login) {
        User findUser = em.find(User.class, user.getId());

        findUser.setLogin(findUser.getLogin() + login);
        
        em.flush();
    }

    @JpaTransaction
    public void plusRecommend(User target, int recommend) {

        User findUser = em.find(User.class, target.getId());

        findUser.setRecommend(findUser.getRecommend() + recommend);

        em.flush();
    }

    @JpaTransaction
    public void checkedRecommend(User user) {

        em.createQuery("Update User u Set u.recid = :recid Where u.id = :id")
          .setParameter("recid", user.getRecid())
          .setParameter("id", user.getId())
          .executeUpdate();
          em.flush();
    }
}