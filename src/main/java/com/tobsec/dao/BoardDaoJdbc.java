package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.List;

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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;

@Repository("boardDao")
public class BoardDaoJdbc implements BoardDao {
    @Resource(name="getBoardMapper")
    private RowMapper<Board> boardMapper;

    // 자동 sequence 반환을 쉽게 하기 위한 jdbcInsert
    private SimpleJdbcInsert jdbcInsert;

    private NamedParameterJdbcTemplate nameJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.nameJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("BOARD").usingGeneratedKeyColumns("board_no");
    }

    public int insertBoard(Board board) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

        // SimpleJDBCInsert 사용
        return jdbcInsert.executeAndReturnKey(param).intValue();

        // KeyHolder에 자동생성 키 담기
        /*
        KeyHolder keyHolder = new GeneratedKeyHolder();

        nameJdbcTemplate.update("Insert Into BOARD (writer_id, content, board_gubun) Values (:writerId, :content, 1)", param, keyHolder);
        
        return keyHolder.getKey().intValue();
        */
    }

    
    public int getMaxBoardNo() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.nameJdbcTemplate.queryForObject("Select Max(board_no) As max_boardno From BOARD", param, Integer.class);
    }

    public void updateBoard(Board board) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(board);

        this.nameJdbcTemplate.update("Update BOARD Set content = :content Where board_no = :boardNo ", param);
    }

    public void deleteBoard(int boardNo) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("boardNo", boardNo);

        this.nameJdbcTemplate.update("Delete From BOARD Where board_no = :boardNo", param);
    }

    public void deleteAll() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        this.nameJdbcTemplate.update("Delete From BOARD", param);

        initAuto();
    }

    private void initAuto() {
        this.nameJdbcTemplate.execute("ALTER TABLE BOARD AUTO_INCREMENT = 1", new PreparedStatementCallback() {
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

        return this.nameJdbcTemplate.queryForObject("SELECT AUTO_INCREMENT From INFORMATION_SCHEMA.TABLES Where TABLE_SCHEMA = :databaseName And TABLE_NAME = 'BOARD'", param, Integer.class);
    }

    public int countAll() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.nameJdbcTemplate.queryForObject("Select Count(*) As cnt From BOARD", param, Integer.class);
    }

    public Board getBoard(int boardNo) {

        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("boardNo", boardNo);

        return this.nameJdbcTemplate.queryForObject("Select * From BOARD Where board_no = :boardNo", param, this.boardMapper);
    }

    public List<Board> getAllBoardList() {
        MapSqlParameterSource param = new MapSqlParameterSource();

        return this.nameJdbcTemplate.query("Select * From BOARD Where board_no", param, this.boardMapper);
    }

    /**
     * 작성자의 모든 리스트
     */
    public List<Board> getAllBoardListByUserId(String writerId) {
        MapSqlParameterSource param = new MapSqlParameterSource();

        param.addValue("writerId", writerId);

        return this.nameJdbcTemplate.query("Select * From BOARD Where writer_id = :writerId Where board_no", param, this.boardMapper);
    }
}