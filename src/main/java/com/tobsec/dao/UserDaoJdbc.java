package com.tobsec.dao;

import java.util.*;
import com.tobsec.model.*;

import javax.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("userDao")
public class UserDaoJdbc extends DaoSupport implements UserDao {
    @Resource(name="getUserMapper")
    private RowMapper<User> getUserMapper;
    
    
    /**
     * 메소드에 @Autowired를 적용하여, 단순 필드에 주입하는 것이 아닌, Template 생성에만 DataSource 사용
     */
    /*
    @Autowired
    public void setDataSource(DataSource dataSource) {
        getNamedParameterJdbcTemplate() = new NamedParameterJdbcTemplate(dataSource);
    }
    */

    private SqlParameterSource makeParam(User user) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", user.getId());
        paramSource.addValue("name", user.getName());
        paramSource.addValue("password", user.getPassword());
        paramSource.addValue("level", user.getLevel().getValue());
        paramSource.addValue("login", user.getLogin());
        paramSource.addValue("recommend", user.getRecommend());
        paramSource.addValue("email", user.getEmail());
        paramSource.addValue("recid", user.getRecid());

        return paramSource;
    }

    private SqlParameterSource nullParam() {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        return paramSource;
    }

    public int addUser(User user)  {
        return getNamedParameterJdbcTemplate().update("Insert Into User (id, name, password, level, login, recommend, email, recid) Values (:id, :name, :password, :level, :login, :recommend, :email, :recid) ", makeParam(user));
    }

    public int updateUser(User user) {
        return getNamedParameterJdbcTemplate().update("Update User Set name = :name, password = :password, email = :email Where id = :id ", makeParam(user));
    }

    public int deleteUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);
        return getNamedParameterJdbcTemplate().update("Delete From User Where id = :id ", paramSource);
    }

    // 임시 사용(원래는 무조건 하나씩만)
    public void plusLogin(User user, int login) throws RuntimeException {
        getNamedParameterJdbcTemplate().update("Update User Set login = login + " + login + " Where id = :id ", makeParam(user));
    }

    // 임시 사용(원래는 무조건 하나씩만)
    public void plusRecommend(User target, int recommend) throws RuntimeException {
        getNamedParameterJdbcTemplate().update("Update User Set recommend = recommend + " + recommend + " Where id = :id ", makeParam(target));
    }

    public void checkedRecommend(User user) throws RuntimeException {
        getNamedParameterJdbcTemplate().update("Update User Set recid = :recid Where id = :id ", makeParam(user));
    }

    public void upgradeLevel(User user) {
        getNamedParameterJdbcTemplate().update("Update User Set level = :level Where id = :id ", makeParam(user));
    }

    public int deleteAll() {
        return getNamedParameterJdbcTemplate().update("Delete From User ", nullParam());
    }

    @Transactional(readOnly=true)
    public int countUserAll() {
        return getNamedParameterJdbcTemplate().queryForObject("Select Count(*) As allcnt From USER ", nullParam(), Integer.class);
    }

    @Transactional(readOnly=true)
    public int countUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);

        return getNamedParameterJdbcTemplate().queryForObject("Select Count(*) As cnt From USER Where id = :id ", paramSource, Integer.class);
    }

    @Transactional(readOnly=true)
    public int countUserCondition(String option) {
        return getNamedParameterJdbcTemplate().queryForObject("Select Count(*) As wherecnt From USER Where 1 = 1 " + option, nullParam(), Integer.class);
    }

    @Transactional(readOnly=true)
    public User getUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);

        return getNamedParameterJdbcTemplate().queryForObject("Select * From USER Where id = :id", paramSource, this.getUserMapper);
    }

    @Transactional(readOnly=true)
    public List<User> selectUserAll() {
        return getNamedParameterJdbcTemplate().query("Select * From USER Order By id", nullParam(), this.getUserMapper);
    }

    /**
     * 조건에 따른 조회 쿼리
     */
    @Transactional(readOnly=true)
    public List<User> selectUserCondition(String option) {
        return getNamedParameterJdbcTemplate().query("Select * From USER Where 1 = 1" + option, nullParam(), this.getUserMapper);
    }
}