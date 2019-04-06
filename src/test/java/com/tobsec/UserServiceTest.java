package com.tobsec;

import com.tobsec.model.Level;
import com.tobsec.model.User;

import com.tobsec.context.AppConfig;
import com.tobsec.service.UserService;

import com.tobsec.service.exception.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class UserServiceTest {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("userServiceTest")
    private UserService userServiceTest;

    private List<User> list;
    private List<User> testList;

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

        testList = new ArrayList<User>(Arrays.asList(
            new User("1", "사용자1", "1", Level.SILVER, 51, 2, "a@a.com"),
            new User("2", "사용자2", "2", Level.GOLD, 53, 31, "b@b.com"),
            new User("3", "사용자3", "3", Level.BRONZE, 0, 0, "c@c.com"),
            new User("4", "사용자4", "4", Level.SILVER, 55, 0, "d@d.com"),
            new User("5", "사용자5", "5", Level.GOLD, 51, 36, "e@e.com"),
            new User("6", "사용자6", "6", Level.BRONZE, 0, 0, "f@f.com")
        ));
    }

    @Test(expected=EmptyResultException.class)
    public void serviceAddTest() {
        userService.deleteAll();

        int count = userService.countAll();

        assertThat(count, is(0));

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userService.addUser(user);

        count = userService.countAll();

        assertThat(count, is(1));

        user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userService.addUser(user);

        fail("테스트 실패");
    }

    // LevelUpFailException 뜰 거임
    @Test(expected=LevelUpFailException.class)
    public void upgradeLevel() {
        userService.deleteAll();

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userService.addUser(user);

        userService.upgradeLevels(user);

        user = userService.getUser("1");

        assertThat(user.getLevel(), is(Level.BRONZE));

        user.setLevel(Level.PLATINUM);

        userService.upgradeLevels(user);
    }

    @Test
    public void recommendTest() {
        userService.deleteAll();

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");
        
        userService.addUser(user);

        user = userService.getUser(user.getId());

        assertThat(user.getRecommend(), is(0));

        User user2 = new User("2", "사용자2", "2", Level.BRONZE, 0, 0, "b@b.com");
        user2.setRecid("1");
        userService.addUser(user2);

        user2 = userService.getUser(user2.getId());

        user = userService.getUser(user.getId());
        assertThat(user.getRecommend(), is(1));
        assertThat(user2.getRecid(), is(user.getId()));
    }

    @Test
    public void customTest() {
        userServiceTest.deleteAll();

        int count = userServiceTest.countAll();

        assertThat(count, is(0));

        for( User user : testList ) {
            userServiceTest.addUser(user);
        }

        User infoUser = null;
        for( User user : testList ) {
            infoUser = userServiceTest.getUser(user.getId());

            assertThat(infoUser.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void countConditionTest() {
        userServiceTest.deleteAll();

        for( User user : testList ) {
            userServiceTest.addUser(user);
        }

        int count = userServiceTest.countUserLevel(Level.BRONZE, "EQ", null);

        assertThat(count, is(2));

        count = userServiceTest.countUserLevel(Level.BRONZE, "OV", null);

        assertThat(count, is(4));

        count = userServiceTest.countUserLevel(Level.SILVER, "UN", null);
        
        assertThat(count, is(2));

        count = userServiceTest.countUserLevel(Level.SILVER, "BT", Level.GOLD);

        assertThat(count, is(4));
    }
}