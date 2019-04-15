package com.tobsec.dao;

import com.tobsec.model.User;

import java.util.List;

public interface UserDao {
    public int addUser(User user);
    public int updateUser(User user);
    public int deleteUser(String id);
    public int deleteAll();
    public int countUserAll();
    public int countUser(String id);
    public int countUserCondition(String option);
    public User getUser(String id);
    public List<User> selectUserAll();
    public List<User> selectUserCondition(String option);
    public void upgradeLevel(User user);
    public void plusLogin(User user, int login);
    public void plusRecommend(User target, int recommend);
    public void checkedRecommend(User user);
}