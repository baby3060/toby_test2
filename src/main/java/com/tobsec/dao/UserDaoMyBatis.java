package com.tobsec.dao;

import com.tobsec.model.User;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoMyBatis implements UserDao {
    private SqlSession sqlSession;

    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int addUser(User user) {
        return this.sqlSession.insert("mapper.mybatis.UserMapper.addUser", user);
    }

    public int updateUser(User user) {
        throw new UnsupportedOperationException();
    }

    public int deleteUser(String id) {
        throw new UnsupportedOperationException();
    }
    public int deleteAll() {
        throw new UnsupportedOperationException();
    }
    public int countUserAll() {
        throw new UnsupportedOperationException();
    }
    public int countUser(String id) {
        throw new UnsupportedOperationException();
    }
    public int countUserCondition(String option) {
        throw new UnsupportedOperationException();
    }
    public User getUser(String id) {
        throw new UnsupportedOperationException();
    }
    public List<User> selectUserAll() {
        throw new UnsupportedOperationException();
    }
    public List<User> selectUserCondition(String option) {
        throw new UnsupportedOperationException();
    }
    public void upgradeLevel(User user) {
        throw new UnsupportedOperationException();
    }

    public void plusLogin(User user, int login) {
        throw new UnsupportedOperationException();
    }

    public void plusRecommend(User target, int recommend) {
        throw new UnsupportedOperationException();
    }

    public void checkedRecommend(User user) {
        throw new UnsupportedOperationException();
    }
}