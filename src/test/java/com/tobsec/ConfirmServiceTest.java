package com.tobsec;

import com.tobsec.model.*;

import com.tobsec.context.AppConfig;
import com.tobsec.service.UserService;
import com.tobsec.service.ConfirmService;
import com.tobsec.service.ConfirmServiceImpl;

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

import org.springframework.transaction.annotation.Transactional;

import org.springframework.test.annotation.Rollback;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class ConfirmServiceTest  implements ParentTest  {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    private List<Confirm> list;

    @Autowired
    private ConfirmService confirmService;

    @Before
    @Rollback(false)
    public void setUp() {
        confirmService.deleteAll();
        userService.deleteAll();

        User user = new User("1", "김길동", "비번1", Level.BRONZE, 49, 0, "a@n.com");

        userService.addUser(user);

        list = new ArrayList<Confirm>(Arrays.asList(
            new Confirm(user, 20190403, 1, "테스트1"),
            new Confirm(user, 20190403, 2, "테스트2"),
            new Confirm(user, 20190403, 3, "테스트3"),
            new Confirm(user, 20190403, 4, "테스트4"),
            new Confirm(user, 20190404, 1, "테스트5"),
            new Confirm(user, 20190405, 1, "테스트6")
        ));
    }

    @After
    @Rollback(false)
    public void close() {
        confirmService.deleteAll();
        userService.deleteAll();
    }

    @Test(expected=EmptyResultException.class)
    public void nonUserAddTest() {
        User user = userService.getUser("1");

        Confirm confirm = new Confirm(user, 20190407, "테스트");

        int confirmCount1 = confirmService.countAllUser(user.getId());

        assertThat(confirmCount1, is(0));

        confirmService.addConfirm(confirm);

        assertThat(confirm.getConfirm_seq(), is(1));

        confirmCount1 = confirmService.countAllUser(user.getId());

        assertThat(confirmCount1, is(1));

        User user2 = new User("2", "김길동", "비번2", Level.BRONZE, 49, 0, "b@n.com");

        Confirm confirmExcep = new Confirm(user2, 20190407, "테스트");

        confirmService.addConfirm(confirmExcep);

        fail("EmptyResultException expected");
    }

    @Test
    public void insertTest() {
        for( Confirm confirm : list ) {
            confirmService.addConfirm(confirm);
        }

        int count = confirmService.countAllUser("1");

        assertThat(list.size(), is(count));

        List<Confirm> searchList = confirmService.selectAllList();
        assertThat(list.size(), is(searchList.size()));

        Confirm temp1 = null;
        Confirm tempSearch = null;

        for( int i = 0; i < list.size(); i++ ) {
            temp1 = list.get(i);
            tempSearch = searchList.get(i);

            assertThat(temp1, equalTo(tempSearch));
        }
    }

}