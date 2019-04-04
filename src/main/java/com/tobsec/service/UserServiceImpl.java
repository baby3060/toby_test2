package com.tobsec.service;

import com.tobsec.model.User;
import com.tobsec.model.Level;

import com.tobsec.dao.UserDao;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.tobsec.service.exception.*;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public void addUser(User user) throws EmptyResultException{
        int result = 0;

        if(user == null) {
            result = 0;
        } else {
            if( userDao.countUser(user.getId()) == 0 ) {
                
                // 등급은 BRONZE로
                user.setLevel(Level.BRONZE);
                // 신규 유저 등록일 경우 무조건 둘 다 0
                user.setLogin(0);
                user.setRecommend(0);

                result = userDao.addUser(user);
            }
        }

        if( result < 1 ) {
            throw new EmptyResultException("저장 중 에러가 발생하였습니다. 데이터가 저장되지 않았습니다.");
        }
    }

    @Override
    public void updateUser(User user) throws EmptyResultException{
        int result = 0;

        if(user == null) {
            result = 0;
        } else {
            if( userDao.countUser(user.getId()) == 1 ) {
                result = userDao.updateUser(user);
            }
        }

        if( result < 1 ) {
            throw new EmptyResultException("저장 중 에러가 발생하였습니다. 데이터가 저장되지 않았습니다.");
        }
    }

    @Override
    public void deleteUser(User user) throws EmptyResultException{
        int result = 0;

        if(user == null) {
            result = 0;
        } else {
            if( userDao.countUser(user.getId()) == 1 ) {
                result = userDao.deleteUser(user.getId());
            }
        }

        if( result < 1 ) {
            throw new EmptyResultException("저장 중 에러가 발생하였습니다. 데이터가 저장되지 않았습니다.");
        }
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public int countUser(String id) {
        return userDao.countUser(id);
    }

    @Override
    public int countAll() {
        return userDao.countUserAll();
    }
}