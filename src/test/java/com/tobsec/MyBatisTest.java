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
    private List<User> list;

    @Autowired
    private UserDao userDaoBatis;

    @Autowired
    private SqlSession sqlSession;

    @Before
    public void setUp() {
        list = new ArrayList<User>(Arrays.asList(
            new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com"),
            new User("2", "사용자2", "2", Level.BRONZE, 0, 0, "b@b.com"),
            new User("3", "사용자3", "3", Level.BRONZE, 0, 0, "c@c.com"),
            new User("4", "사용자4", "4", Level.BRONZE, 0, 0, "d@d.com"),
            new User("5", "사용자5", "5", Level.BRONZE, 0, 0, "e@e.com"),
            new User("6", "사용자6", "6", Level.BRONZE, 0, 0, "f@f.com")
        ));
    }

    @Test
    public void addUserBatis() {
        userDaoBatis.deleteAll();

        int count = userDaoBatis.countUserAll();

        assertThat(count, is(0));

        User user = new User("1", "사용자1", "1", Level.SILVER, 0, 0, "a@a.com");

        userDaoBatis.addUser(user);

        count = userDaoBatis.countUserAll();

        assertThat(count, is(1));

        User userSave = userDaoBatis.getUser("1");

        assertThat(user.getLevel(), is(userSave.getLevel()));
        assertThat(userSave.getLevel().getValue(), is(2));
    }

    @Test
    public void selectAllTest() {
        userDaoBatis.deleteAll();

        for( User user : list ) {
            userDaoBatis.addUser(user);
        }

        List<User> getList = userDaoBatis.selectUserAll();

        assertThat(getList.size(), is(6));
        assertThat(getList.size(), is(getList.size()));

        int idx = 0;

        for(User user : list) {
            checkUserSame(user, getList.get(idx));
            idx++;
        }

        int count = userDaoBatis.countUserCondition(" And id Like \'%1%\' ");

        assertThat(count, is(1));

        List<User> optionList = userDaoBatis.selectUserCondition(" And id Like \'%3%\' ");

        assertThat(optionList.size(), is(1));

        User getUser = optionList.get(0);

        assertThat(getUser.getId(), is("3"));
        assertThat(getUser.getName(), is("사용자3"));
        assertThat(getUser.getPassword(), is("3"));
        assertThat(getUser.getEmail(), is("c@c.com"));
    }

    private void checkUserSame(User user1, User user2) {
        assertTrue(user1.equals(user2));
    }

}