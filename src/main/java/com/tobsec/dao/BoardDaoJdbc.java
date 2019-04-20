package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCallback;  
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class BoardDaoJdbc extends DaoSupport implements BoardDao {
    @Resource(name="getBoardMapper")
    private RowMapper<Board> boardMapper;

    

    public int insertBoard(Board board) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

        // SimpleJDBCInsert 사용
        return jdbcInsert.executeAndReturnKey(param).intValue();

        // KeyHolder에 자동생성 키 담기
        /*
        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedParameterJdbcTemplate().update("Insert Into BOARD (writer_id, content, board_gubun) Values (:writerId, :content, 1)", param, keyHolder);
        
        return keyHolder.getKey().intValue();
        */
    }

    
    public int getMaxBoardNo() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("board", "getMaxBoardNo"), param, Integer.class);
    }

    public void updateBoard(Board board) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

        this.getNamedParameterJdbcTemplate().update(sqlService.findSql("board", "updateBoard"), param);
    }

    public void deleteBoard(int boardNo) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("boardNo", boardNo);

        this.getNamedParameterJdbcTemplate().update(sqlService.findSql("board", "deleteBoard"), param);
    }

    public void deleteAll() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        this.getNamedParameterJdbcTemplate().update(sqlService.findSql("board", "deleteAll"), param);

        initAuto();
    }

    private void initAuto() {
        alterBoardNo(1);
    }

    /**
     * AUTO_INCREMENT Value Get
     */
    public int getAutoValue(String databaseName) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("databaseName", databaseName);

        return this.getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("board", "getAutoValue"), param, Integer.class);
    }

    public int countAll() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("board", "countAll"), param, Integer.class);
    }

    public int countBoard(int boardNo) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("boardNo", boardNo);

        return this.getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("board", "countBoard"), param, Integer.class);
    }

    public Board getBoard(int boardNo) {

        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("boardNo", boardNo);

        return this.getNamedParameterJdbcTemplate().queryForObject(sqlService.findSql("board", "getBoard"), param, this.boardMapper);
    }

    public List<Board> getAllBoardList() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.getNamedParameterJdbcTemplate().query(sqlService.findSql("board", "getAllBoardList"), param, this.boardMapper);
    }

    public void alterBoardNo(final int autoInit) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("init", autoInit);

        this.getNamedParameterJdbcTemplate().execute(sqlService.findSql("board", "alterBoardNo"), paramMap, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement pstmt) throws SQLException {
                return pstmt.executeUpdate();  
            }
        });
    }

    /**
     * 작성자의 모든 리스트
     */
    public List<Board> getAllBoardListByUserId(String writerId) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("writerId", writerId);

        return this.getNamedParameterJdbcTemplate().query(sqlService.findSql("board", "getAllBoardListByUserId"), param, this.boardMapper);
    }
}