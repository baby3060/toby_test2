package com.tobsec;

import com.tobsec.model.Level;
import com.tobsec.model.User;

import com.tobsec.context.AppConfig;
import com.tobsec.service.UserService;
import com.tobsec.service.UserServiceImpl;

import com.tobsec.common.Log;
import com.tobsec.service.exception.*;

import java.sql.SQLException;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.slf4j.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class UserServiceTest implements ParentTest  {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Log
    protected Logger serviceLogger;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("testService")
    private UserService userServiceTest;

    private List<User> list;
    private List<User> testList;
    List<User> users;

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
            new User("6", "사용자6", "6", Level.BRONZE, 0, 0, "f@f.com"),
            new User("7", "사용자7", "7", Level.BRONZE, 0, 0, "g@f.com")
        ));

        users = new ArrayList<User>(Arrays.asList(
            new User("1", "김길동", "비번1", Level.BRONZE, 49, 0, "a@n.com"),
            new User("2", "배길동", "비번2", Level.SILVER, 60, 31, "b@n.com"),
            new User("3", "이길동", "비번3", Level.BRONZE, 45, 0, "c@n.com"),
            new User("4", "고길동", "비번4", Level.GOLD, 60, 33, "d@n.com"),
            new User("5", "최길동", "비번5", Level.SILVER, 54, 30, "e@n.com"),
            new User("6", "박길동", "비번6", Level.BRONZE, 51, 0, "f@n.com")
        ));
    }

    @Test(expected=EmptyResultException.class)
    public void serviceAddTest() {
        userService.deleteAll();

        int count = userService.countAll();

        assertThat(count, is(0));

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userService.addUser(user);

        User newUser = userService.getUser("1");

        assertThat("1", is(not(newUser.getPassword())));

        count = userService.countAll();

        assertThat(count, is(1));

        user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userService.addUser(user);

        fail("테스트 실패");
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        userServiceTest.deleteAll();

        for(User user : users) {
            userServiceTest.addUser(user);
        }

        try {
            userServiceTest.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch(UserServiceImpl.TestUserServiceException e) {
            
        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpgrade = userService.getUser(user.getId());
        
        if( upgraded ) {
            assertThat(userUpgrade.getLevel(), is(user.getLevel().getNextLevel()));
        } else {
            assertThat(userUpgrade.getLevel(), is(user.getLevel()));
        }
        
    }

    @Test
    public void upgradeLevel() {
        userService.deleteAll();

        User user = new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com");

        userService.addUser(user);

        userService.upgradeLevels();

        user = userService.getUser("1");

        assertThat(user.getLevel(), is(Level.BRONZE));

        user.setLevel(Level.PLATINUM);

        userService.upgradeLevels();

        userService.plusLogin(user);
        userService.plusLogin(user);

        user = userService.getUser("1");


        assertThat(user.getLogin(), is(2));
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

        int count = userService.countAll();

        assertThat(count, is(0));

        for( User user : testList ) {
            userServiceTest.addUser(user);
        }

        count = userService.countAll();

        assertThat(count, is(7));

        User infoUser = null;
        for( User user : testList ) {
            infoUser = userService.getUser(user.getId());

            assertThat(infoUser.getId(), is(user.getId()));
            assertThat(infoUser.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void selectUserAllTest() {
        userServiceTest.deleteAll();

        int count = userService.countAll();

        assertThat(count, is(0));

        for( User user : testList ) {
            userServiceTest.addUser(user);
        }

        List<User> innerResult = userService.selectUserAll();

        assertThat(innerResult.size(), is(testList.size()));
    }

    @Test
    public void countConditionTest() {
        userService.deleteAll();

        for( User user : testList ) {
            userServiceTest.addUser(user);
        }

        int count = userService.countUserLevel(Level.BRONZE, "EQ", null);

        assertThat(count, is(3));

        count = userService.countUserLevel(Level.BRONZE, "OV", null);

        assertThat(count, is(4));

        count = userService.countUserLevel(Level.GOLD, "UN", null);
        
        assertThat(count, is(5));

        count = userService.countUserLevel(Level.SILVER, "BT", Level.GOLD);

        assertThat(count, is(4));
    }

    @Test    
    @Transactional
    public void compositeTest() {
        serviceLogger.info("Start compositeTest");

        userService.deleteAll();

        for( User user : testList ) {
            userServiceTest.addUser(user);
        }

        User user = new User("100301030103103", "테스트", "121231", Level.BRONZE, 0, 0, "");

        int count = userService.countAll();

        assertThat(count, is(testList.size()));

        try {
            userService.addUserNew(user);
        } catch(Exception e) {}
        
        count = userService.countAll();

        assertThat(count, is(testList.size()));

        serviceLogger.info("End compositeTest");
    }

    @Test
    public void accessTest() {
        try {
            User user = new User("5", "사용자5", "5", Level.GOLD, 51, 36, "e@e.com");

            userService.goldOverAcceable(user);

            user = new User("5", "사용자5", "5", Level.SILVER, 51, 36, "e@e.com");

            userService.goldOverAcceable(user);
        } catch(RuntimeException e) {
            serviceLogger.error("accessTest Exception : " + e.getMessage());
        }
    }

    @Test
    public void machingTest() {
        userService.deleteAll();

        User user = new User("5", "사용자5", "5", Level.BRONZE, 0, 36, "e@e.com");

        userService.addUser(user);

        String encrypPass = passwordEncoder.encode("5");
        
        assertTrue(userService.passwordCheck("5", user.getId()));
        assertTrue(passwordEncoder.matches("5", encrypPass));
    }

}