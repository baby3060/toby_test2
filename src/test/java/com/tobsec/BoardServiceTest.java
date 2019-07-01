package com.tobsec;

import com.tobsec.model.*;

import com.tobsec.context.AppConfig;
import com.tobsec.service.UserService;
import com.tobsec.service.BoardService;
import com.tobsec.service.ConfirmService;

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

import org.slf4j.Logger;
import com.tobsec.common.Log;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.annotation.Commit;

import javax.persistence.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class BoardServiceTest implements ParentTest  {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ConfirmService confirmService;

    @Autowired
    private UserService userService;

    @Autowired
    private BasicDataSource dataSource;

    @Log
    protected Logger boardLogger;

    private List<Board> boardList;

    @Before
    @Rollback(false)
    public void setUp() {
        confirmService.deleteAll();
        boardService.deleteAll();
        userService.deleteAll();
        int count = userService.countAll();
        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");
        userService.addUser(user);

        boardList = new ArrayList<Board>(Arrays.asList(
            new Board(user, "테스트", 0),
            new Board(user, "테스트2", 0)
        ));

    }

    @After
    @Rollback(false)
    public void closeTest() {
        boardService.deleteAll();

        userService.deleteAll();
    }

    @Test(expected=EmptyResultException.class)
    public void insertTestException() {
        /*
        String dbUrl = dataSource.getUrl();
        dbUrl = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();
        */

        int increVal = boardService.getIncreValue("");
        int countAll = boardService.countAll();

        assertThat(increVal, is(0));
        assertThat(countAll, is(0));

        User user = new User("2", "사용자2", "2", Level.BRONZE, 0, 0, "a@a.com");

        Board board = new Board();
        board.setWriter(user);
        board.setContent("테스트");

        boardService.addBoard(board);

        fail("No User Board Insert");
    }

    @Test
    public void insertTestNormal() {
        User user = userService.getUser("1");

        assertThat(user, is(not(nullValue())));
        /*
        String dbUrl = dataSource.getUrl();
        dbUrl = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();
        */

        boardService.deleteAll();

        int increVal = boardService.getIncreValue("");
        int countAll = boardService.countAll();

        assertThat(increVal, is(0));
        assertThat(countAll, is(0));

        Board board = new Board();
        board.setWriter(user);
        board.setContent("테스트");

        boardService.addBoard(board);
        assertThat(board.getBoardNo(), is(1L));
        boardLogger.info("Board Print : " + board.toString());

        List<Board> allList = boardService.selectAll();
        boardLogger.info("allList Print " + allList.toString());

        Board findBoard = boardService.getBoard(1L);
        assertThat(board, equalTo(findBoard));

        User userGet = userService.getUser("1");
        
        increVal = boardService.getIncreValue("");
        countAll = boardService.countAll();

        assertThat(increVal, is(1));
        assertThat(countAll, is(1));
    }

    @Test
    public void updateBoardTest() {
        int initVal = boardService.getIncreValue("");
        assertThat(initVal, is(0));

        User user = userService.getUser("1");

        assertThat(user, is(not(nullValue())));

        for( Board board : boardList ) {
            boardService.addBoard(board);
        }
        
        List<Board> allList = boardService.selectAll();
        assertThat(allList.size(), is(2));
        boardLogger.info("allList Print " + allList.toString());

        Long maxSeqno = boardService.getMaxBoardNo();

        boardLogger.info("MaxSeqno : " + maxSeqno);

        assertThat(allList.get(0).getBoardNo(), is(1L));
        assertThat(allList.get(1).getBoardNo(), is(2L));

        int count = boardService.countBoard(allList.get(1));

        assertThat(count, is(1));

        Board getBoard = boardService.getBoard(maxSeqno);

        assertThat(allList.get(1), equalTo(getBoard));

        getBoard.setContent("수정(테스트)");

        boardService.updateContent(getBoard);

        Board getBoardAfter = boardService.getBoard(maxSeqno);

        assertThat(getBoard, equalTo(getBoardAfter));
        
    }

    @Test
    public void deleteBoardTest() {

        User user = userService.getUser("1");

        assertThat(user, is(not(nullValue())));

        /*
        String dbUrl = dataSource.getUrl();
        dbUrl = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();
        */
        int increVal = boardService.getIncreValue("");
        assertThat(increVal, is(0));

        Board board = new Board();
        board.setWriter(user);
        board.setContent("테스트");

        boardService.addBoard(board);

        board = new Board();
        board.setWriter(user);
        board.setContent("테스트1");

        boardService.addBoard(board);

        board = new Board();
        board.setWriter(user);
        board.setContent("테스트2");

        boardService.addBoard(board);

        board = new Board();
        board.setWriter(user);
        board.setContent("테스트3");

        boardService.addBoard(board);

        increVal = boardService.getIncreValue("");
        assertThat(increVal, is(4));

        Long maxSeqno = boardService.getMaxBoardNo();
        assertThat(maxSeqno, is(4L));

        Board deleteBoard = boardService.getBoard(3L);

        boardService.deleteBoard(deleteBoard);

        int countAll = boardService.countAll();

        increVal = boardService.getIncreValue("");
        assertThat(increVal, is(4));
        assertThat(countAll, is(3));
    }

    @Test
    public void deleteAndInsertTest() {
        
        User user = userService.getUser("1");

        Board board = new Board();
        board.setWriter(user);
        board.setContent("테스트");
        boardService.addBoard(board);

        board = new Board();
        board.setWriter(user);
        board.setContent("테스트1");
        boardService.addBoard(board);

        /*
        String dbUrl = dataSource.getUrl();
        dbUrl = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?")).toUpperCase();
        */

        Board deleteBoard = boardService.getBoard(1L);
        boardService.deleteBoard(deleteBoard);

        int increVal = boardService.getIncreValue("");
        assertThat(increVal, is(2));
        deleteBoard = boardService.getBoard(2L);
        boardService.deleteBoard(deleteBoard);
        increVal = boardService.getIncreValue("");
        assertThat(increVal, is(0));
    }
}