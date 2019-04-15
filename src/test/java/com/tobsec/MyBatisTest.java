package com.tobsec;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.UserDao;
import com.tobsec.dao.UserDaoMyBatis;
import com.tobsec.model.User;
import com.tobsec.model.Level;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.ibatis.session.SqlSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class MyBatisTest {
    @Autowired
    private UserDao userDao;

    private UserDaoMyBatis userDaoBatis = new UserDaoMyBatis();

    @Autowired
    private SqlSession sqlSession;

    @Before
    public void setUp() {
        userDaoBatis.setSqlSession(sqlSession);
    }

    @Test
    public void addUserBatis() {
        userDao.deleteAll();

        int count = userDao.countUserAll();

        assertThat(count, is(0));

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userDaoBatis.addUser(user);

        count = userDao.countUserAll();

        assertThat(count, is(1));

        User userSave = userDao.getUser("1");

        assertThat(user.getLevel(), is(userSave.getLevel()));
    }


}