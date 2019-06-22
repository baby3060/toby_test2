package com.tobsec;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.*;
import com.tobsec.model.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
public class ConfirmDaoTest  implements ParentTest  {
    private List<Confirm> list;

    @Autowired
    private ConfirmDao confirmDao;

    @Autowired
    private UserDao userDao;

    @Log
    protected Logger logger;

    @Before
    @Rollback(false)
    public void setUp() {
        confirmDao.deleteAllUser("1");
        confirmDao.deleteAllUser("2");
        userDao.deleteAll();

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");
        User user2 = new User("2", "사용자2", "2", Level.BRONZE, 0, 0, "b@b.com");
        
        userDao.addUser(user);
        userDao.addUser(user2);
        
        list = new ArrayList<Confirm>(Arrays.asList(
            new Confirm(user, 20190403, 1, "테스트1"),
            new Confirm(user, 20190403, 2, "테스트2"),
            new Confirm(user, 20190403, 3, "테스트3"),
            new Confirm(user, 20190403, 4, "테스트4"),
            new Confirm(user, 20190404, 1, "테스트5"),
            new Confirm(user2, 20190404, 1, "테스트5_2"),
            new Confirm(user, 20190405, 1, "테스트6")
        ));
    }

    @After
    @Rollback(false)
    public void close() {
        confirmDao.deleteAllUser("1");
        confirmDao.deleteAllUser("2");
        userDao.deleteAll();
    }

    @Test
    public void deleteAndCount() {
        User user = userDao.getUser("1");

        confirmDao.deleteAllUser("1");

        int count = confirmDao.countAllUser("1");

        assertThat(count, is(0));

        Confirm confirm = new Confirm();
        confirm.setApproval(user);
        confirm.setConfirm_date(20190403);
        confirm.setConfirm_seq(1);
        confirm.setContent("테스트");

        confirmDao.addConfirm(confirm);

        count = confirmDao.countAllUser("1");

        assertThat(count, is(1));
    }

    @Test
    public void listCount() {
        confirmDao.deleteAllUser("1");
        confirmDao.deleteAllUser("2");

        int count = confirmDao.countAllUser("1");

        assertThat(count, is(0));

        for( Confirm confirm : list ) {
            confirmDao.addConfirm(confirm);
        }

        count = confirmDao.countAllUser("1");

        assertThat(count, is(6));

        // id 2가 하나 끼어있으니까 같아선 안 된다.
        assertThat(list.size(), is(not(count)));

        int count_2 = confirmDao.countAllUser("2");
        assertThat(list.size(), is(count + count_2));
    }

    @Test
    public void solveDetail() {
        confirmDao.deleteAllUser("1");
        confirmDao.deleteAllUser("2");

        int count = confirmDao.countAllUser("1");

        assertThat(count, is(0));

        for( Confirm confirm : list ) {
            confirmDao.addConfirm(confirm);
        }

        count = confirmDao.countAllUser("1");

        assertThat(count, is(6));

        // id 2가 하나 끼어있으니까 같아선 안 된다.
        assertThat(list.size(), is(not(count)));

        int count_2 = confirmDao.countAllUser("2");
        assertThat(list.size(), is(count + count_2));
    }

    @Test
    public void solveDetail2() {
        confirmDao.deleteAllUser("1");
        confirmDao.deleteAllUser("2");

        for( Confirm confirm : list ) {
            confirmDao.addConfirm(confirm);
        }

        List<Confirm> noSolveListDt = confirmDao.selectNoSolveBetDt(20190101, 20191231);

        logger.info(noSolveListDt.size() + "");

        List<Confirm> noSolveUser_1 = confirmDao.selectNoSolveByUser("1");
        List<Confirm> noSolveUser_2 = confirmDao.selectNoSolveByUser("2");
        
        assertThat(list.size(), is(noSolveUser_1.size() + noSolveUser_2.size()));
        assertThat(noSolveListDt.size(), is(list.size()));
    }

    @Test
    public void solveTest3() {
        confirmDao.deleteAllUser("1");
        confirmDao.deleteAllUser("2");

        for( Confirm confirm : list ) {
            confirmDao.addConfirm(confirm);
        }

        List<Confirm> noSolveUser_1 = confirmDao.selectNoSolveByUser("1");
        List<Confirm> noSolveUser_2 = confirmDao.selectNoSolveByUser("2");

        confirmDao.updateConfirmSolve(noSolveUser_1.get(0));
        confirmDao.updateConfirmSolve(noSolveUser_1.get(1));

        List<Confirm> noSolveListDt = confirmDao.selectNoSolveBetDt(20190101, 20191231);

        assertThat(noSolveListDt.size(), is(not(noSolveUser_1.size() + noSolveUser_2.size())));

        // 유저가 확인해준거
        List<Confirm> solveUser_1BT = confirmDao.selectSolveCheckUserSDt("1", 20190101, 20191231);

        assertThat(solveUser_1BT.size(), is(0));

        // 유저가 확인해줬음
        confirmDao.updateUserOk(noSolveUser_1.get(0));
        confirmDao.updateUserOk(noSolveUser_1.get(1));

        solveUser_1BT = confirmDao.selectSolveCheckUserSDt("1", 20190101, 20191231);

        assertThat(solveUser_1BT.size(), is(2));
    }

    @Test
    public void proTest() {
        confirmDao.deleteAllUser("1");
        confirmDao.deleteAllUser("2");

        for( Confirm confirm : list ) {
            confirmDao.addConfirm(confirm);
        }

        int count = confirmDao.countAllUser("1");
        count = count + confirmDao.countAllUser("2");

        assertThat(count, is(7));

        List<Confirm> noSolveUser_1 = confirmDao.selectNoSolveByUser("1");

        // 문제 해결
        confirmDao.updateConfirmSolve(noSolveUser_1.get(0));
        confirmDao.updateConfirmSolve(noSolveUser_1.get(1));

        List<Confirm> deleteTarget = confirmDao.selectNoSolveBetDt(20190101, 20191231);

        for( Confirm confirm : deleteTarget ) {
            confirmDao.deleteConfirm(confirm);
        }
        
        count = confirmDao.countAllUser("1");
        count = count + confirmDao.countAllUser("2");

        assertThat(count, is(2));
        int empty_cnt = confirmDao.countEmptySolveContent();
        assertThat(count, is(empty_cnt));

        confirmDao.updateUserOk(noSolveUser_1.get(0));
        confirmDao.updateUserOk(noSolveUser_1.get(1));

        confirmDao.filedSolveContent();

        empty_cnt = confirmDao.countEmptySolveContent();
        assertThat(empty_cnt, is(0));

    }
}