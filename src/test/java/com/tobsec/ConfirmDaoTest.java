package com.tobsec;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.ConfirmDao;
import com.tobsec.model.Confirm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class ConfirmDaoTest {
    private List<Confirm> list;

    @Autowired
    private ConfirmDao confirmDao;

    @Before
    public void setUp() {
        list = new ArrayList<Confirm>(Arrays.asList(
            new Confirm("1", 20190403, 1, "테스트1"),
            new Confirm("1", 20190403, 2, "테스트2"),
            new Confirm("1", 20190403, 3, "테스트3"),
            new Confirm("1", 20190403, 4, "테스트4"),
            new Confirm("1", 20190404, 1, "테스트5"),
            new Confirm("2", 20190404, 1, "테스트5_2"),
            new Confirm("1", 20190405, 1, "테스트6")
        ));
    }

    @Test
    public void deleteAndCount() {
        confirmDao.deleteAllUser("1");

        int count = confirmDao.countAllUser("1");

        assertThat(count, is(0));

        Confirm confirm = new Confirm();
        confirm.setId("1");
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

        List<Confirm> noSolveListDt = confirmDao.selectNoSolveBetDt(20190101, 20190601);

        assertThat(noSolveListDt.size(), is(list.size()));

        List<Confirm> noSolveUser_1 = confirmDao.selectNoSolveByUser("1");
        List<Confirm> noSolveUser_2 = confirmDao.selectNoSolveByUser("2");

        assertThat(noSolveListDt.size(), is(noSolveUser_1.size() + noSolveUser_2.size()));

        confirmDao.updateConfirmSolve(noSolveUser_1.get(0));
        confirmDao.updateConfirmSolve(noSolveUser_1.get(1));

        noSolveListDt = confirmDao.selectNoSolveBetDt(20190101, 20190601);

        assertThat(noSolveListDt.size(), is(not(noSolveUser_1.size() + noSolveUser_2.size())));

        // 유저가 확인해준거
        List<Confirm> solveUser_1BT = confirmDao.selectSolveCheckUserSDt("1", 20190101, 20190601);

        assertThat(solveUser_1BT.size(), is(0));

        // 유저가 확인해줬음
        confirmDao.updateUserOk(noSolveUser_1.get(0));
        confirmDao.updateUserOk(noSolveUser_1.get(1));

        solveUser_1BT = confirmDao.selectSolveCheckUserSDt("1", 20190101, 20190601);

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

        List<Confirm> deleteTarget = confirmDao.selectNoSolveBetDt(20190101, 20190601);

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