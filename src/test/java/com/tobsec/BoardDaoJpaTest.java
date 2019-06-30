package com.tobsec;

import com.tobsec.common.JpaTransaction;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.BoardDao;
import com.tobsec.dao.UserDao;
import com.tobsec.model.User;
import com.tobsec.model.Board;
import com.tobsec.model.Level;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.After;

import org.apache.commons.dbcp2.BasicDataSource;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.springframework.test.annotation.Rollback;

import javax.persistence.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
@Transactional
public class BoardDaoJpaTest implements ParentTest{
    @Autowired
    private BasicDataSource dataSource;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BoardDao boardDaoJpa;

    @Before
    @Rollback(false)
    public void setUp() {
        // 모두 삭제 한 다음에는 auto_increment의 값은 항상 1로 초기화시킴
        boardDaoJpa.deleteAll();

        boardDaoJpa.alterBoardNo(1L);

        userDao.deleteAll();

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");
        User user2 = new User("2", "사용자2", "2", Level.BRONZE, 0, 0, "a@a.com");

        userDao.addUser(user);
        userDao.addUser(user2);
    }

    @After
    @Rollback(false)
    public void closeTest() {
        boardDaoJpa.deleteAll();
        boardDaoJpa.alterBoardNo(1L);
        userDao.deleteAll();
    }

    @Test
    public void boardInsertTest() {
        String dbUrl = dataSource.getUrl();

        String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();

        // AUTO_INCREMENT의 현재값 조회
        int incrementVal = boardDaoJpa.getAutoValue(dbName);

        assertThat(incrementVal, is(1));

        int count = boardDaoJpa.countAll();

        assertThat(count, is(0));

        Board board = new Board();
        board.setContent("Test");
        // board.setWriter(userDao.getUser("1"));
        board.setId("1");

        boardDaoJpa.insertBoard(board);

        incrementVal = boardDaoJpa.getAutoValue(dbName);

        assertThat(incrementVal, is(2));

        board = new Board();
        board.setContent("Test2");
        // board.setWriter(userDao.getUser("1"));
        board.setId("1");

        boardDaoJpa.insertBoard(board);

        count = boardDaoJpa.countAll();

        assertThat(count, is(2));

        incrementVal = boardDaoJpa.getAutoValue(dbName);

        assertThat(incrementVal, is(3));
    }

    /*
    @Test
    public void updateBoard() {
        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));

        Long insertNo = boardDaoJpa.insertBoard(board);

        assertThat(insertNo, is(1L));

        Board boardGet = boardDaoJpa.getBoard(insertNo);

        assertThat(boardGet.getContent(), is(board.getContent()));
        assertThat(boardGet.getWriter().getId(), is(board.getWriter().getId()));

        boardGet.setContent("테스트 수정하였음");

        boardDaoJpa.updateBoard(boardGet);

        boardGet = boardDaoJpa.getBoard(insertNo);

        assertThat(boardGet.getContent(), is("테스트 수정하였음"));
    }

    @Test
    public void listTest() {
        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));

        boardDaoJpa.insertBoard(board);

        board = new Board();
        board.setContent("Test2");
        board.setWriter(userDao.getUser("1"));

        boardDaoJpa.insertBoard(board);

        board = new Board();
        board.setContent("Test3");
        board.setWriter(userDao.getUser("2"));

        boardDaoJpa.insertBoard(board);

        List<Board> allListBoard = boardDaoJpa.getAllBoardList();
        assertThat(allListBoard.size(), is(3));

        List<Board> user1Board = boardDaoJpa.getAllBoardListByUserId("1");
        assertThat(user1Board.size(), is(2));

        List<Board> user2Board = boardDaoJpa.getAllBoardListByUserId("2");
        assertThat(user2Board.size(), is(1));

        assertThat(allListBoard.size(), is(user1Board.size() + user2Board.size()));
    }
    */
}