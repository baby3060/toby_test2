package com.tobsec.dao;

import com.tobsec.model.User;

import java.util.List;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

@Repository("userDao")
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
        return this.sqlSession.update("mapper.mybatis.UserMapper.updateUser", user);
    }

    public int deleteUser(String id) {
        return this.sqlSession.delete("mapper.mybatis.UserMapper.deleteUser", id);
    }

    public int deleteAll() {
        return this.sqlSession.delete("mapper.mybatis.UserMapper.deleteUserAll");
    }

    public int countUserAll() {
        return this.sqlSession.selectOne("mapper.mybatis.UserMapper.countAllUser");
    }

    public int countUser(String id) {
        return this.sqlSession.selectOne("mapper.mybatis.UserMapper.countUser", id);
    }

    public int countUserCondition(String option) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("option", option);

        return this.sqlSession.selectOne("mapper.mybatis.UserMapper.countUserOption", param);
    }

    public User getUser(String id) {
        return this.sqlSession.selectOne("mapper.mybatis.UserMapper.getUser", id);
    }
    public List<User> selectUserAll() {
        return this.sqlSession.selectList("mapper.mybatis.UserMapper.selectUserAll");
    }

    public List<User> selectUserCondition(String option) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("option", option);

        return this.sqlSession.selectList("mapper.mybatis.UserMapper.selectUserOption", param);
    }
    
    public void upgradeLevel(User user) {
        this.sqlSession.update("mapper.mybatis.UserMapper.upgradeLevel", user);
    }

    public void plusLogin(User user, int login) {
        user.setLogin(user.getLogin() + login);
        this.sqlSession.update("mapper.mybatis.UserMapper.plusLogin", user);
    }

    public void plusRecommend(User target, int recommend) {
        target.setRecommend(target.getRecommend() + recommend);
        this.sqlSession.update("mapper.mybatis.UserMapper.plusRecommend", target);
    }

    public void checkedRecommend(User user) {
        this.sqlSession.update("mapper.mybatis.UserMapper.checkedRecommend", user);
    }
}