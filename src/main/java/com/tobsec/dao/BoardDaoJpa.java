package com.tobsec.dao;

import com.tobsec.model.Board;
import javax.persistence.*;
import com.tobsec.common.JpaTransaction;

import java.math.BigInteger;
import java.util.*;

import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.slf4j.Logger;
import com.tobsec.common.Log;

@Repository("boardDao")
public class BoardDaoJpa implements BoardDao {
    @PersistenceContext
    private EntityManager em;

    @Log
    protected Logger daoLogger;

    public void insertBoard(Board board) {
        if( board.getBoardNo() == null || board.getBoardNo() == 0L ) {
            em.persist(board);
        } else {
            em.merge(board);
        }
    }

    public void updateBoard(Board board) {
        
    }

    public void deleteBoard(Long boardNo) {
        Board findBoard = em.find(Board.class, boardNo);
        em.remove(findBoard);
    }
    
    public void deleteAll() {
        em.createQuery("Delete From Board").executeUpdate();
    }

    public int getAutoValue(String databaseName) {

        Query query = em.createNativeQuery("Select board_seq_val " +
                                " From TABLE_SEQ_KEY " +
                                " Where sequence_name = 'board_seq'");

        BigInteger result = (BigInteger)query.getSingleResult();
        
        return result.intValue();
    }

    public int countAll() {
        TypedQuery<Long> query = em.createQuery("Select Count(*) From Board", Long.class);

        Long result = query.getSingleResult();

        return (int)result.longValue();
    }

    public Long getMaxBoardNo() {
        Query query = em.createQuery("Select Max(b.boardNo) From Board b");
        
        Long result = (Long)query.getSingleResult();

        return result;
    }

    public Board getBoard(Long boardNo) {
        
        Board temp = em.find(Board.class, boardNo);
        return temp;
    }

    public List<Board> getAllBoardList() {
        return em.createQuery("Select a From Board a Inner Join a.writer u Order By a.boardNo ", Board.class).getResultList();
    }

    public List<Board> getAllBoardListByUserId(String writerId) {
        return em.createNativeQuery("Select * From Board a Inner Join User b On (b.id = a.writer_id) Where b.id = :writerId Order By a.board_no ", Board.class)
                 .setParameter("writerId", writerId)
                 .getResultList();
    }

    public int countBoard(Long boardNo) {
        TypedQuery<Long> query = em.createQuery("Select Count(b) From Board b Where b.boardNo = :boardNo", Long.class).setParameter("boardNo", boardNo);

        Long result = query.getSingleResult();

        int actResult = (int)result.longValue();

        return actResult;
    }

    public void alterBoardNo(Long autoInit) {
        em.createNativeQuery("Update TABLE_SEQ_KEY Set board_seq_val = :autoInit Where sequence_name = 'board_seq' ").setParameter("autoInit", autoInit).executeUpdate();
    }
}