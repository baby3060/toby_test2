package com.tobsec;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.BoardDao;
import com.tobsec.dao.UserDao;
import com.tobsec.model.Board;
import com.tobsec.model.User;
import com.tobsec.model.Level;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.test.annotation.Rollback;

import org.slf4j.Logger;
import com.tobsec.common.Log;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class BoardDaoTest implements ParentTest {
    
    @Autowired
    private BoardDao boardDao;

    @Log
    protected Logger boardLogger;

    @Autowired
    private UserDao userDao;

    @Before
    @Rollback(false)
    public void setUp() {
        
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
    public void completeClose() {
        boardDao.deleteAll();
        boardDao.alterBoardNo(0L);
        userDao.deleteAll();
    }

    @Test
    public void boardInsertTest() {
        User user = userDao.getUser("1");

        Board board11 = new Board();
        board11.setContent("Test11");
        board11.setWriter(user);
        boardDao.insertBoard(board11);

        // boardLogger.info("Board List Is " + user.getBoardList().toString());
        boardLogger.info("Board List Is " + boardDao.getAllBoardListByUserId(user.getId()));
    }

    
    @Test
    public void updateBoard() {
        
        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));

        boardDao.insertBoard(board);

        long insertNo = board.getBoardNo();

        assertThat(insertNo, is(1L));

        Board boardGet = boardDao.getBoard(insertNo);

        assertThat(boardGet.getContent(), is(board.getContent()));
        assertThat(boardGet.getWriter().getId(), is(board.getWriter().getId()));

        boardGet.setContent("테스트 수정하였음");

        boardDao.updateBoard(boardGet);

        Board boardGet2 = boardDao.getBoard(insertNo);

        assertThat(boardGet2.getContent(), is("테스트 수정하였음"));
        
    }
    
}