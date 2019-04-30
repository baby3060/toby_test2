package com.tobsec.dao;

import com.tobsec.model.User;

import java.util.List;

public interface UserDao {
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(String id);
    int deleteAll();
    int countUserAll();
    int countUser(String id);
    int countUserCondition(String option);
    User getUser(String id);
    List<User> selectUserAll();
    List<User> selectUserCondition(String option);
    void upgradeLevel(User user);
    void plusLogin(User user, int login);
    void plusRecommend(User target, int recommend);
    void checkedRecommend(User user);
}