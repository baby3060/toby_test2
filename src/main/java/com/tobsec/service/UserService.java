package com.tobsec.service;

import java.util.List;

import com.tobsec.model.User;
import com.tobsec.model.Level;

import com.tobsec.service.exception.*;

public interface UserService {
    public void addUser(User user) throws EmptyResultException;
    public void updateUser(User user) throws EmptyResultException;
    public void deleteUser(User user) throws EmptyResultException;
    public void deleteAll();
    public User getUser(String id);
    public int countUser(String id);

    public int countAll();

    public void upgradeLevels(User user) throws RuntimeException;

    public void plusLogin(User user) throws RuntimeException;
    public void plusRecommend(User target, User recoUser) throws RuntimeException;

    // gubun = EQ(동일), OV(초과), UN(미만), BT(사이)
    // 해당 Level 값 기준 수량
    public int countUserLevel(Level level, String gubun, Level toLevel);

    public List<User> selectUserAll();
}