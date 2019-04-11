package com.tobsec.service.sql;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class SqlServiceImpl implements SqlService {
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    @Autowired
    public SqlServiceImpl(SqlReader sqlReader, SqlRegistry sqlRegistry) {
        this.sqlReader = sqlReader;
        this.sqlRegistry = sqlRegistry;
    }

    // 객체 생성되고, 의존관계 주입 후에 실행되는 메소드
    @PostConstruct
    public void init() {
        this.sqlReader.readSql(this.sqlRegistry);
    }

    public String findSql(String gubun, String key) {
        String sql = "";
        try {
            sql = sqlRegistry.getSql(gubun, key);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sql;
    }
}