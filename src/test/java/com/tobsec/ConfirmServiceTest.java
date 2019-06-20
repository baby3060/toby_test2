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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class ConfirmServiceTest  implements ParentTest  {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    private ConfirmService confirmService;

    
    @Test(expected=EmptyResultException.class)
    public void nonUserAddTest() {
        userService.deleteAll();
        confirmService.deleteAll();
        
        int count = userService.countAll();

        assertThat(count, is(0));

        User user = new User("1", "김길동", "비번1", Level.BRONZE, 49, 0, "a@n.com");

        userService.addUser(user);

        Confirm confirm = new Confirm(user.getId(), 20190407, "테스트");

        int confirmCount1 = confirmService.countAllUser(user.getId());

        assertThat(confirmCount1, is(0));

        confirmService.addConfirm(confirm);

        assertThat(confirm.getConfirm_seq(), is(1));

        confirmCount1 = confirmService.countAllUser(user.getId());

        assertThat(confirmCount1, is(1));

        Confirm confirmExcep = new Confirm("2", 20190407, "테스트");

        confirmService.addConfirm(confirmExcep);

        fail("EmptyResultException expected");
    }

}