package com.tobsec.dao;

import com.tobsec.model.Board;
import javax.persistence.*;
import com.tobsec.common.JpaTransaction;

import java.math.BigInteger;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository("boardDaoJpa")
public class BoardDaoJpa implements BoardDao {
    @PersistenceContext
    private EntityManager em;

    @JpaTransaction
    public int insertBoard(Board board) {
        em.persist(board);
        return board.getBoardNo();
    }

    @JpaTransaction
    public void updateBoard(Board board) {
        Board findBoard = em.find(Board.class, board.getBoardNo());

        findBoard.setContent(board.getContent());
    }

    @JpaTransaction
    public void deleteBoard(int boardNo) {
        Board findBoard = em.find(Board.class, boardNo);
        em.remove(findBoard);
    }

    @JpaTransaction
    public void deleteAll() {
        em.createQuery("Delete From Board").executeUpdate();
    }

    public int getAutoValue(String databaseName) {
        Query query = em.createNativeQuery("SELECT AUTO_INCREMENT " +
                                " From INFORMATION_SCHEMA.TABLES " +
                                " Where TABLE_SCHEMA = :schema " +
                                " And TABLE_NAME = 'BOARD'");

        query = query.setParameter("schema", databaseName);

        BigInteger result = (BigInteger)query.getSingleResult();
        
        return result.intValue();
    }

    public int countAll() {
        TypedQuery<Long> query = em.createQuery("Select Count(b) From Board b", Long.class);

        return (int)query.getSingleResult().longValue();
    }

    public int getMaxBoardNo() {
        TypedQuery<Long> query = em.createQuery("Select Max(b.board_no) From Board b", Long.class);

        return (int)query.getSingleResult().longValue();
    }

    public Board getBoard(int boardNo) {
        return em.find(Board.class, boardNo);
    }

    public List<Board> getAllBoardList() {
        return em.createNativeQuery("Select * From Board a Inner Join User b On (b.id = a.writer_id) Order By a.board_no ", Board.class).getResultList();
    }

    public List<Board> getAllBoardListByUserId(String writerId) {
        return em.createNativeQuery("Select * From Board a Inner Join User b On (b.id = a.writer_id) Where b.id = :writerId Order By a.board_no ", Board.class)
                 .setParameter("writerId", writerId)
                 .getResultList();
    }

    public int countBoard(int boardNo) {
        TypedQuery<Long> query = em.createQuery("Select Count(b) From Board b Where b.boardNo = :boardNo", Long.class).setParameter("boardNo", boardNo);

        return (int)query.getSingleResult().longValue();
    }

    public void alterBoardNo(int autoInit) {
        em.createNativeQuery("ALTER TABLE BOARD AUTO_INCREMENT = :autoInit").setParameter("autoInit", autoInit).executeUpdate();
    }
}