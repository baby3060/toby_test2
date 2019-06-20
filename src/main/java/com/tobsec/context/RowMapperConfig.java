package com.tobsec.context;

import com.tobsec.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.jdbc.core.RowMapper;

@Configuration
public class RowMapperConfig { 
    @Bean
    public RowMapper<User> getUserMapper() {
        return new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));
                user.setEmail(rs.getString("email"));
                // user.setRecid(rs.getString("recid"));
                return user;
            }
        };
    } 
    
    @Bean
    public RowMapper<Confirm> getConfirmMapper() {
        return new RowMapper<Confirm>() {
            public Confirm mapRow(ResultSet rs, int rowNum) throws SQLException {
                Confirm confirm = new Confirm();

                confirm.setId(rs.getString("id"));
                confirm.setConfirm_date(rs.getInt("confirm_date"));
                confirm.setConfirm_seq(rs.getInt("confirm_seq"));
                confirm.setConfirm_time(rs.getString("confirm_time"));
                confirm.setContent(rs.getString("content"));
                confirm.setSolve_content(rs.getString("solve_content"));
                confirm.setCheckflagad(rs.getString("checkflagad"));
                confirm.setCheckflagus(rs.getString("checkflagus"));
                confirm.setSolve_timestamp(rs.getTimestamp("solve_timestamp"));

                return confirm;
            }
        };
    } 

    @Bean
    public RowMapper<Board> getBoardMapper() {
        return new RowMapper<Board>() {
            public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
                Board board = new Board();
                User user = new User();

                board.setBoardNo(rs.getInt("board_no"));
                board.setContent(rs.getString("content"));
                board.setBoardGubun(rs.getInt("board_gubun"));
                board.setWriteTime(rs.getTimestamp("write_time"));

                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));
                user.setRecid(rs.getString("recid"));

                board.setWriter(user);

                return board;
            }
        };
    }
}