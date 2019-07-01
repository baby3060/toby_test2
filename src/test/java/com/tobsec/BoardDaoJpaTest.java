package com.tobsec;

import com.tobsec.common.JpaTransaction;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.ConfirmDao;
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
    private BoardDao boardDao;

    @Autowired
    private ConfirmDao confirmDao;

    @Before
    @Rollback(false)
    public void setUp() {
        // 모두 삭제 한 다음에는 auto_increment의 값은 항상 1로 초기화시킴
        confirmDao.deleteAll();

        boardDao.deleteAll();

        boardDao.alterBoardNo(0L);

        userDao.deleteAll();

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");
        User user2 = new User("2", "사용자2", "2", Level.BRONZE, 0, 0, "a@a.com");

        userDao.addUser(user);
        userDao.addUser(user2);
    }

    @After
    @Rollback(false)
    public void closeTest() {
        confirmDao.deleteAll();
        boardDao.deleteAll();
        boardDao.alterBoardNo(0L);
        userDao.deleteAll();
    }

    @Test
    public void boardInsertTest() {
        String dbUrl = dataSource.getUrl();

        String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();

        // AUTO_INCREMENT의 현재값 조회
        int incrementVal = boardDao.getAutoValue(dbName);

        assertThat(incrementVal, is(0));

        int count = boardDao.countAll();

        assertThat(count, is(0));

        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));
        

        boardDao.insertBoard(board);

        incrementVal = boardDao.getAutoValue(dbName);

        assertThat(incrementVal, is(1));

        board = new Board();
        board.setContent("Test2");
        board.setWriter(userDao.getUser("1"));

        boardDao.insertBoard(board);

        count = boardDao.countAll();

        assertThat(count, is(2));

        incrementVal = boardDao.getAutoValue(dbName);

        assertThat(incrementVal, is(2));
    }

    
    @Test
    public void updateBoard() {
        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));

        Long insertNo = boardDao.insertBoard(board);

        assertThat(insertNo, is(1L));

        Board boardGet = boardDao.getBoard(insertNo);

        assertThat(boardGet.getContent(), is(board.getContent()));
        assertThat(boardGet.getWriter().getId(), is(board.getWriter().getId()));

        boardGet.setContent("테스트 수정하였음");

        boardDao.updateBoard(boardGet);

        boardGet = boardDao.getBoard(insertNo);

        assertThat(boardGet.getContent(), is("테스트 수정하였음"));
    }

    @Test
    public void listTest() {
        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));

        boardDao.insertBoard(board);

        board = new Board();
        board.setContent("Test2");
        board.setWriter(userDao.getUser("1"));

        boardDao.insertBoard(board);

        board = new Board();
        board.setContent("Test3");
        board.setWriter(userDao.getUser("2"));

        boardDao.insertBoard(board);

        List<Board> allListBoard = boardDao.getAllBoardList();
        assertThat(allListBoard.size(), is(3));

        List<Board> user1Board = boardDao.getAllBoardListByUserId("1");
        assertThat(user1Board.size(), is(2));

        List<Board> user2Board = boardDao.getAllBoardListByUserId("2");
        assertThat(user2Board.size(), is(1));

        assertThat(allListBoard.size(), is(user1Board.size() + user2Board.size()));
    }
    
}