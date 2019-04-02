package com.tobsec.dao;

import com.tobsec.model.*;

import java.util.*;

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
}