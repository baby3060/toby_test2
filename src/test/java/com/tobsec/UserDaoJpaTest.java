package com.tobsec;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.tobsec.context.AppConfig;
import com.tobsec.dao.UserDao;
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
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
@Transactional
public class UserDaoJpaTest implements ParentTest {
    private List<User> list;

    @Autowired
    private UserDao userDaoJpa;

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
    public void count() {
        userDaoJpa.deleteAll();

        int count = userDaoJpa.countUserAll();

        assertThat(count, is(0));
    }

    @Test
    public void insertTest() {
        userDaoJpa.deleteAll();

        int count = userDaoJpa.countUserAll();

        assertThat(count, is(0));

        for( User user : list ) {
            userDaoJpa.addUser(user);
        }

        count = userDaoJpa.countUserAll();

        assertThat(count, is(6));
    }

    @Test
    public void updateTest() {
        userDaoJpa.deleteAll();
        int count = userDaoJpa.countUserAll();

        assertThat(count, is(0));

        for( User user : list ) {
            userDaoJpa.addUser(user);
        }

        count = userDaoJpa.countUserAll();

        assertThat(count, is(6));

        User user1 = list.get(0);
        User user2 = list.get(1);
        User user3 = list.get(2);
        User user4 = list.get(3);
        User user5 = list.get(4);
        User user6 = list.get(5);

        User changeUser = list.get(1);

        changeUser.setName("사용자(변경)2");

        userDaoJpa.updateUser(changeUser);

        User findUser = userDaoJpa.getUser(user2.getId());

        assertThat(user1, is(equalTo(list.get(0))));
        assertThat(findUser.getName(), is(not("사용자2")));
        assertThat(user3, is(equalTo(list.get(2))));
        assertThat(user4, is(equalTo(list.get(3))));
        assertThat(user5, is(equalTo(list.get(4))));
        assertThat(user6, is(equalTo(list.get(5))));
    }

    @Test
    public void deleteTest() {
        userDaoJpa.deleteAll();

        int count = userDaoJpa.countUserAll();

        assertThat(count, is(0));

        for( User user : list ) {
            userDaoJpa.addUser(user);
        }

        count = userDaoJpa.countUserAll();

        assertThat(count, is(6));

        count = userDaoJpa.countUser(list.get(0).getId());

        assertThat(count, is(1));

        userDaoJpa.deleteUser(list.get(0).getId());
        userDaoJpa.deleteUser(list.get(2).getId());
        userDaoJpa.deleteUser(list.get(3).getId());

        count = userDaoJpa.countUserAll();

        assertThat(count, is(3));

        count = userDaoJpa.countUser(list.get(0).getId());

        assertThat(count, is(0));
    }

    @Test
    public void getUserTest() {
        userDaoJpa.deleteAll();
        for( User user : list ) {
            userDaoJpa.addUser(user);
        }
        
        
        User user1 = list.get(0);
        User user2 = list.get(1);
        User user3 = list.get(2);
        User user4 = list.get(3);
        User user5 = list.get(4);
        User user6 = list.get(5);

        User anotherUser1 = userDaoJpa.getUser(user1.getId());
        User anotherUser2 = userDaoJpa.getUser(user2.getId());
        User anotherUser3 = userDaoJpa.getUser(user3.getId());
        User anotherUser4 = userDaoJpa.getUser(user4.getId());
        User anotherUser5 = userDaoJpa.getUser(user5.getId());
        User anotherUser6 = userDaoJpa.getUser(user6.getId());

        assertThat(user1, equalTo(anotherUser1));
        assertThat(user2, equalTo(anotherUser2));
        assertThat(user3, equalTo(anotherUser3));
        assertThat(user4, equalTo(anotherUser4));
        assertThat(user5, equalTo(anotherUser5));
        assertThat(user6, equalTo(anotherUser6));
    }

}