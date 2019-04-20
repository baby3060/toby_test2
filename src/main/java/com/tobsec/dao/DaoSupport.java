package com.tobsec.dao;

import com.tobsec.service.sql.SqlService;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.annotation.Resource;

// NamedParameterJdbcTemplate을 사용하였으니 NamedParameterJdbcDaoSupport 사용
public class DaoSupport extends NamedParameterJdbcDaoSupport {
    @Autowired
    protected SqlService sqlService;

    // 자동 sequence 반환을 쉽게 하기 위한 jdbcInsert
    protected SimpleJdbcInsert jdbcInsert;

    @Resource
    public void setDs(DataSource dataSource) {
        setDataSource(dataSource);

        this.jdbcInsert = new SimpleJdbcInsert(dataSource);
    }

}