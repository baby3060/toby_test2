package com.tobsec;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.BoardDao;
import com.tobsec.dao.UserDao;
import com.tobsec.model.Board;
import com.tobsec.model.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class BoardDaoTest implements ParentTest {
    @Autowired
    private BoardDao boardDao;
    
    @Autowired
    private BasicDataSource dataSource;

    @Autowired
    private UserDao userDao;

    @Test
    public void boardInsertTest() {
        // 모두 삭제 한 다음에는 auto_increment의 값은 항상 1로 초기화시킴
        boardDao.deleteAll();

        boardDao.alterBoardNo(1);

        String dbUrl = dataSource.getUrl();

        String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();

        // AUTO_INCREMENT의 현재값 조회
        int incrementVal = boardDao.getAutoValue(dbName);

        assertThat(incrementVal, is(1));

        int count = boardDao.countAll();

        assertThat(count, is(0));

        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));

        boardDao.insertBoard(board);

        count = boardDao.countAll();

        assertThat(count, is(1));

        incrementVal = boardDao.getAutoValue(dbName);

        assertThat(incrementVal, is(2));
    }

    @Test
    public void updateBoard() {
        // 모두 삭제 한 다음에는 auto_increment의 값은 항상 1로 초기화시킴
        boardDao.deleteAll();

        boardDao.alterBoardNo(1);

        Board board = new Board();
        board.setContent("Test");
        board.setWriter(userDao.getUser("1"));

        int insertNo = boardDao.insertBoard(board);

        assertThat(insertNo, is(1));

        Board boardGet = boardDao.getBoard(insertNo);

        assertThat(boardGet.getContent(), is(board.getContent()));
        assertThat(boardGet.getWriter().getId(), is(board.getWriter().getId()));

        boardGet.setContent("테스트 수정하였음");

        boardDao.updateBoard(boardGet);

        boardGet = boardDao.getBoard(insertNo);

        assertThat(boardGet.getContent(), is("테스트 수정하였음"));
    }
}