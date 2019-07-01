package com.tobsec.dao;

import com.tobsec.model.User;

import java.util.List;
import java.util.HashMap;

// import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.session.SqlSessionFactory;

@Repository("userDaoBatis")
public class UserDaoMyBatis extends SqlSessionDaoSupport implements UserDao {
    /*
    // SqlSessionDaoSupport 사용하지 않을 때 주석 해제
    private SqlSession sqlSession;

    // 단위 테스트 대비 설정자 메소드 DI
    @Autowired
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    */

    @Autowired 
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) { 
        super.setSqlSessionFactory(sqlSessionFactory); 
    }

    public int addUser(User user) {
        return this.getSqlSession().insert("mapper.mybatis.UserMapper.addUser", user);
    }

    public int updateUser(User user) {
        return this.getSqlSession().update("mapper.mybatis.UserMapper.updateUser", user);
    }

    public int deleteUser(String id) {
        return this.getSqlSession().delete("mapper.mybatis.UserMapper.deleteUser", id);
    }

    public int deleteAll() {
        return this.getSqlSession().delete("mapper.mybatis.UserMapper.deleteUserAll");
    }

    public int countUserAll() {
        return this.getSqlSession().selectOne("mapper.mybatis.UserMapper.countAllUser");
    }

    public int countUser(String id) {
        return this.getSqlSession().selectOne("mapper.mybatis.UserMapper.countUser", id);
    }

    public int countUserCondition(String option) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("option", option);

        return this.getSqlSession().selectOne("mapper.mybatis.UserMapper.countUserOption", param);
    }

    public User getUser(String id) {
        return this.getSqlSession().selectOne("mapper.mybatis.UserMapper.getUser", id);
    }
    public List<User> selectUserAll() {
        return this.getSqlSession().selectList("mapper.mybatis.UserMapper.selectUserAll");
    }

    public List<User> selectUserCondition(String option) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("option", option);

        return this.getSqlSession().selectList("mapper.mybatis.UserMapper.selectUserOption", param);
    }
    
    public void upgradeLevel(User user) {
        this.getSqlSession().update("mapper.mybatis.UserMapper.upgradeLevel", user);
    }

    public void plusLogin(User user, int login) {
        user.setLogin(user.getLogin() + login);
        this.getSqlSession().update("mapper.mybatis.UserMapper.plusLogin", user);
    }

    public void plusRecommend(User target, int recommend) {
        target.setRecommend(target.getRecommend() + recommend);
        this.getSqlSession().update("mapper.mybatis.UserMapper.plusRecommend", target);
    }

    public void checkedRecommend(User user) {
        this.getSqlSession().update("mapper.mybatis.UserMapper.checkedRecommend", user);
    }
}