package com.tobsec.dao;

import java.util.*;
import com.tobsec.model.*;

import javax.annotation.Resource;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Repository("userDao")
public class UserDaoJdbc implements UserDao {
    @Resource(name="getUserMapper")
    private RowMapper<User> getUserMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * 메소드에 @Autowired를 적용하여, 단순 필드에 주입하는 것이 아닌, Template 생성에만 DataSource 사용
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private SqlParameterSource makeParam(User user) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", user.getId());
        paramSource.addValue("name", user.getName());
        paramSource.addValue("password", user.getPassword());
        paramSource.addValue("level", user.getLevel().getValue());
        paramSource.addValue("login", user.getLogin());
        paramSource.addValue("recommend", user.getRecommend());
        paramSource.addValue("email", user.getEmail());

        return paramSource;
    }

    private SqlParameterSource nullParam() {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        return paramSource;
    }

    public int addUser(User user)  {
        return this.jdbcTemplate.update("Insert Into User (id, name, password, level, login, recommend, email) Values (:id, :name, :password, :level, :login, :recommend, :email) ", makeParam(user));
    }

    public int updateUser(User user) {
        return this.jdbcTemplate.update("Update User Set name = :name, password = :password, email = :email Where id = :id ", makeParam(user));
    }

    public int deleteUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);
        return this.jdbcTemplate.update("Delete From User Where id = :id ", paramSource);
    }

    public int deleteAll() {
        return this.jdbcTemplate.update("Delete From User ", nullParam());
    }

    public int countUserAll() {
        return this.jdbcTemplate.queryForObject("Select Count(*) As allcnt From USER ", nullParam(), Integer.class);
    }

    public int countUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);

        return this.jdbcTemplate.queryForObject("Select Count(*) As cnt From USER Where id = :id ", paramSource, Integer.class);
    }

    public int countUserCondition(String option) {
        return this.jdbcTemplate.queryForObject("Select Count(*) As wherecnt From USER Where 1 = 1 " + option, nullParam(), Integer.class);
    }

    public User getUser(String id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("id", id);

        return this.jdbcTemplate.queryForObject("Select * From USER Where id = :id", paramSource, this.getUserMapper);
    }

    public List<User> selectUserAll() {
        return this.jdbcTemplate.query("Select * From USER Order By id", nullParam(), 
            /**
             * 이것 대신해서 this.getUserMapper 사용하면 됨
             */
            new RowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            }
        );
    }

    /**
     * 조건에 따른 조회 쿼리
     */
    public List<User> selectUserCondition(String option) {
        return this.jdbcTemplate.query("Select * From USER Where 1 = 1" + option, nullParam(), this.getUserMapper);
    }
}