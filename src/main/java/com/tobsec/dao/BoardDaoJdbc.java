package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Repository("boardDao")
public class BoardDaoJdbc implements BoardDao {
    @Resource(name="getBoardMapper")
    private RowMapper<Board> boardMapper;

    // 자동 sequence 반환을 쉽게 하기 위한 jdbcInsert
    private SimpleJdbcInsert jdbcInsert;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource);
        this.jdbcInsert.withTableName("BOARD");
    }

    public int insertBoard(Board board) {
        return 0;
    }

    public void updateBoard(Board board) {

    }

    public void deleteBoard(int boardNo) {

    }
}