package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.PreparedStatementCallback;  
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Repository("boardDao")
public class BoardDaoJdbc implements BoardDao {
    @Resource(name="getBoardMapper")
    private RowMapper<Board> boardMapper;

    private SqlParameterSource makeParam(Board board) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        paramSource.addValue("boardNo", board.getBoardNo());

        return paramSource;
    }

    // 자동 sequence 반환을 쉽게 하기 위한 jdbcInsert
    private SimpleJdbcInsert jdbcInsert;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("BOARD").usingGeneratedKeyColumns("board_no");
    }

    public int insertBoard(Board board) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

        return jdbcInsert.executeAndReturnKey(param).intValue();
    }

    public void updateBoard(Board board) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

        this.jdbcTemplate.update("Update BOARD Set content = :content Where board_no = :boardNo ", param);
    }

    public void deleteBoard(int boardNo) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("boardNo", boardNo);

        this.jdbcTemplate.update("Delete From BOARD Where board_no = :boardNo", param);
    }

    public void deleteAll() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        this.jdbcTemplate.update("Delete From BOARD", param);

        initAuto();
    }

    private void initAuto() {
        this.jdbcTemplate.execute("ALTER TABLE BOARD AUTO_INCREMENT = 1", new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement pstmt) throws SQLException {
                return pstmt.executeUpdate();  
            }
        });
    }

    /**
     * AUTO_INCREMENT Value Get
     */
    public int getAutoValue(String databaseName) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("databaseName", databaseName);

        return this.jdbcTemplate.queryForObject("SELECT AUTO_INCREMENT From INFORMATION_SCHEMA.TABLES Where TABLE_SCHEMA = :databaseName And TABLE_NAME = 'BOARD'", param, Integer.class);
    }

    public int countAll() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.jdbcTemplate.queryForObject("Select Count(*) As cnt From BOARD", param, Integer.class);
    }
}