package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.session.SqlSessionFactory;

// @Repository("boardDao")
public class BoardDaoMyBatis extends SqlSessionDaoSupport implements BoardDao {
    @Autowired 
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) { 
        super.setSqlSessionFactory(sqlSessionFactory); 
    }

    public Long insertBoard(Board board) {
        return (long)this.getSqlSession().insert("mapper.mybatis.BoardMapper.insertBoard", board);
    }

    public void updateBoard(Board board) {
        this.getSqlSession().update("mapper.mybatis.BoardMapper.updateBoard", board);
    }

    public void deleteBoard(Long boardNo) {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("boardNo", boardNo);

        this.getSqlSession().delete("mapper.mybatis.BoardMapper.deleteBoard", param);
    }

    public void deleteAll() {
        this.getSqlSession().delete("mapper.mybatis.BoardMapper.deleteBoardAll");
    }

    public int getAutoValue(String databaseName) {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("databaseName", databaseName);

        return this.getSqlSession().selectOne("mapper.mybatis.BoardMapper.selectAutoBoard", param);
    }

    public int countAll() {
        return this.getSqlSession().selectOne("mapper.mybatis.BoardMapper.countAll");
    }

    public Long getMaxBoardNo() {
        return this.getSqlSession().selectOne("mapper.mybatis.BoardMapper.getMaxNo");
    }

    public Board getBoard(Long boardNo) {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("boardNo", boardNo);

        return this.getSqlSession().selectOne("mapper.mybatis.BoardMapper.getBoard", param);
    }

    public List<Board> getAllBoardList() {
        return this.getSqlSession().selectList("mapper.mybatis.BoardMapper.selectAll");
    }

    public List<Board> getAllBoardListByUserId(String writerId) {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("writerId", writerId);

        return this.getSqlSession().selectList("mapper.mybatis.BoardMapper.selectAllByWriter", param);
    }

    public int countBoard(Long boardNo) {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("boardNo", boardNo);

        return this.getSqlSession().selectOne("mapper.mybatis.BoardMapper.countBoard", param);
    }

    public void alterBoardNo(Long autoInit) {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("autoInit", autoInit);

        this.getSqlSession().update("mapper.mybatis.BoardMapper.alterIncre", param);
    }
}