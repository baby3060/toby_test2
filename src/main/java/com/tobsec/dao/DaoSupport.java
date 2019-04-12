package com.tobsec.dao;

import com.tobsec.service.sql.SqlService;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import org.springframework.beans.factory.annotation.Autowired;

// NamedParameterJdbcTemplate을 사용하였으니 NamedParameterJdbcDaoSupport 사용
public class DaoSupport extends NamedParameterJdbcDaoSupport {
    @Autowired
    protected SqlService sqlService;

    @Autowired
    public void setDs(DataSource dataSource) {
        setDataSource(dataSource);
    }

}