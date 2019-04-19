package com.tobsec;

import com.tobsec.model.*;

import com.tobsec.context.AppConfig;
import com.tobsec.service.UserService;
import com.tobsec.service.BoardService;

import com.tobsec.service.exception.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.After;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.commons.dbcp2.BasicDataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class BoardServiceTest implements ParentTest  {
    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Autowired
    private BasicDataSource dataSource;

    @Before
    public void setUp() {
        userService.deleteAll();

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userService.addUser(user);
    }

    @After
    public void tearDown() {
        boardService.deleteAll();
        userService.deleteAll();
    }

    @Test(expected=EmptyResultException.class)
    public void insertTest() {
        String dbUrl = dataSource.getUrl();
        dbUrl = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();

        boardService.deleteAll();

        int increVal = boardService.getIncreValue(dbUrl);
        int countAll = boardService.countAll();

        assertThat(increVal, is(1));
        assertThat(countAll, is(0));

        Board board = new Board();
        board.setWriterId("2");
        board.setContent("테스트");

        boardService.addBoard(board);

        increVal = boardService.getIncreValue(dbUrl);

        assertThat(increVal, is(2));

        countAll = boardService.countAll();

        assertThat(countAll, is(1));
    }
}