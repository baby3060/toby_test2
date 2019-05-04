package com.tobsec.service;

import java.util.List;

import com.tobsec.model.User;
import com.tobsec.model.Level;

import com.tobsec.service.exception.*;

public interface UserService {
    void addUser(User user) throws EmptyResultException;
    void addUserNew(User user) throws EmptyResultException;
    void updateUser(User user) throws EmptyResultException;
    void deleteUser(User user) throws EmptyResultException;
    void deleteAll();
    
    User getUser(String id);
    int countUser(String id);
    int countAll();

    void upgradeLevels() throws RuntimeException;

    void plusLogin(User user) throws RuntimeException;
    void plusRecommend(User target, User recoUser) throws RuntimeException;

    // gubun = EQ(동일), OV(초과), UN(미만), BT(사이)
    // 해당 Level 값 기준 수량
    int countUserLevel(Level level, String gubun, Level toLevel);
    List<User> selectUserAll();

    void goldOverAcceable(User user);

    /**
     * @param inpPassword 입력받은 패스워드
     * @param userId 패스워드 검사할 User의 Id
     * @return 암호화된 패스워드와 입력받은 패스워드가 일치하는가?
     */
    boolean passwordCheck(String inpPassword, String userId);
}