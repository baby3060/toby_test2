package com.tobsec.dao;

import com.tobsec.model.User;

import java.util.List;

public interface UserDao {
    public int addUser(User user) throws RuntimeException;
    public int updateUser(User user) throws RuntimeException;
    public int deleteUser(String id) throws RuntimeException;
    public int deleteAll() throws RuntimeException;
    public int countUserAll();
    public int countUser(String id);
    public int countUserCondition(String option);
    public User getUser(String id);
    public List<User> selectUserAll();
    public List<User> selectUserCondition(String option);
    public void upgradeLevel(User user);
    public void plusLogin(User user, int login) throws RuntimeException;
    public void plusRecommend(User target, int recommend) throws RuntimeException;
    public void checkedRecommend(User user) throws RuntimeException;
}