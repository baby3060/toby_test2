package com.tobsec.dao;

import java.util.*;
import com.tobsec.model.*;

import javax.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

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
        return getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "addUser"), makeParam(user));
    }

    public int updateUser(User user) {
        return getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "updateUser"), makeParam(user));
    }

    public int deleteUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);
        return getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "deleteUser"), paramSource);
    }

    // 임시 사용(원래는 무조건 하나씩만)
    public void plusLogin(User user, int login) throws RuntimeException {
        user.setLogin(user.getLogin() + login);
        getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "plusLogin"), makeParam(user));
    }

    // 임시 사용(원래는 무조건 하나씩만)
    public void plusRecommend(User target, int recommend) throws RuntimeException {
        target.setRecommend(target.getRecommend() + recommend);
        getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "plusRecommend"), makeParam(target));
    }

    public void checkedRecommend(User user) throws RuntimeException {
        getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "checkedRecommend"), makeParam(user));
    }

    public void upgradeLevel(User user) {
        getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "upgradeLevel"), makeParam(user));
    }

    public int deleteAll() {
        return getNamedParameterJdbcTemplate().update(sqlService.findSql("user", "deleteAll"), nullParam());
    }

    public int countUserAll() {
        return getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("user", "countUserAll"), nullParam(), Integer.class);
    }

    public int countUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);

        return getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("user", "countUser"), paramSource, Integer.class);
    }

    public int countUserCondition(String option) {
        return getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("user", "countUserCondition") + option, nullParam(), Integer.class);
    }

    public User getUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);

        return getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("user", "getUser"), paramSource, this.getUserMapper);
    }

    public List<User> selectUserAll() {
        return getNamedParameterJdbcTemplate().query(sqlService.findSql("user", "selectUserAll"), nullParam(), this.getUserMapper);
    }

    /**
     * 조건에 따른 조회 쿼리
     */
    public List<User> selectUserCondition(String option) {
        return getNamedParameterJdbcTemplate().query(sqlService.findSql("user", "selectUserCondition") + option + " Order By id", nullParam(), this.getUserMapper);
    }
}