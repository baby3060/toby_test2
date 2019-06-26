package com.tobsec;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.common.Log;
import org.slf4j.Logger;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.UserDao;
import com.tobsec.dao.ConfirmDao;
import com.tobsec.model.User;
import com.tobsec.model.Confirm;
import com.tobsec.model.Level;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.*;

import org.springframework.test.annotation.Rollback;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
@Transactional
public class ConfirmDaoJpaTest implements ParentTest {
    @Log
    protected Logger jpaTestLogger;

    private List<Confirm> list;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ConfirmDao confirmDaoJpa;

    @Before
    @Rollback(false)
    public void setUp() {
        confirmDaoJpa.deleteAllUser("1");
        confirmDaoJpa.deleteAllUser("2");
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
        confirmDaoJpa.deleteAllUser("1");
        confirmDaoJpa.deleteAllUser("2");
        userDao.deleteAll();
    }

    @Test
    public void deleteAndCount() {        
        User user = userDao.getUser("1");

        int count = confirmDaoJpa.countAllUser("1");

        assertThat(count, is(0));

        Confirm confirm = new Confirm();
        confirm.setApproval(user);
        confirm.setConfirm_date(20190403);
        confirm.setConfirm_seq(1);
        confirm.setContent("테스트");

        confirmDaoJpa.addConfirm(confirm);

        count = confirmDaoJpa.countAllUser("1");

        assertThat(count, is(1));
    }

    @Test
    public void solveDetail() {
        for( Confirm confirm : list ) {
            confirmDaoJpa.addConfirm(confirm);
        }

        List<Confirm> noSolveListDt = confirmDaoJpa.selectNoSolveBetDt(20190101, 20191231);

        List<Confirm> noSolveUser_1 = confirmDaoJpa.selectNoSolveByUser("1");
        List<Confirm> noSolveUser_2 = confirmDaoJpa.selectNoSolveByUser("2");
        
        assertThat(list.size(), is(noSolveUser_1.size() + noSolveUser_2.size()));
        assertThat(noSolveListDt.size(), is(list.size()));
    }

    @Test
    public void solveTest2() {
        for( Confirm confirm : list ) {
            confirmDaoJpa.addConfirm(confirm);
        }

        List<Confirm> noSolveUser_1 = confirmDaoJpa.selectNoSolveByUser("1");
        List<Confirm> noSolveUser_2 = confirmDaoJpa.selectNoSolveByUser("2");

        confirmDaoJpa.updateConfirmSolve(noSolveUser_1.get(0));
        confirmDaoJpa.updateConfirmSolve(noSolveUser_1.get(1));

        List<Confirm> noSolveListDt = confirmDaoJpa.selectNoSolveBetDt(20190101, 20191231);

        assertThat(noSolveListDt.size(), is(not(noSolveUser_1.size() + noSolveUser_2.size())));

        // 유저가 확인해준거
        List<Confirm> solveUser_1BT = confirmDaoJpa.selectSolveCheckUserSDt("1", 20190101, 20191231);

        assertThat(solveUser_1BT.size(), is(0));

        
        // 유저가 확인해줬음
        confirmDaoJpa.updateUserOk(noSolveUser_1.get(0));
        confirmDaoJpa.updateUserOk(noSolveUser_1.get(1));

        Confirm getConfirm = confirmDaoJpa.getConfirm("1", 20190403, 1);

        solveUser_1BT = confirmDaoJpa.selectSolveCheckUserSDt("1", 20190101, 20191231);
        
        assertThat(solveUser_1BT.size(), is(2));
        
    }

    @Test
    public void proTest() {
        for( Confirm confirm : list ) {
            confirmDaoJpa.addConfirm(confirm);
        }

        List<Confirm> noSolveUser_1 = confirmDaoJpa.selectNoSolveByUser("1");

        // 문제 해결
        confirmDaoJpa.updateConfirmSolve(noSolveUser_1.get(0));
        confirmDaoJpa.updateConfirmSolve(noSolveUser_1.get(1));

        List<Confirm> deleteTarget = confirmDaoJpa.selectNoSolveBetDt(20190101, 20191231);

        for( Confirm confirm : deleteTarget ) {
            confirmDaoJpa.deleteConfirm(confirm);
        }
        
        int count = confirmDaoJpa.countAllUser("1");
        count = count + confirmDaoJpa.countAllUser("2");

        assertThat(count, is(2));
        int empty_cnt = confirmDaoJpa.countEmptySolveContent();
        assertThat(count, is(empty_cnt));

        confirmDaoJpa.updateUserOk(noSolveUser_1.get(0));
        confirmDaoJpa.updateUserOk(noSolveUser_1.get(1));

        confirmDaoJpa.filedSolveContent();

        empty_cnt = confirmDaoJpa.countEmptySolveContent();
        assertThat(empty_cnt, is(0));
    }

}