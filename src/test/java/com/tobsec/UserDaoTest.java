package com.tobsec;

import com.tobsec.context.AppConfig;

import com.tobsec.dao.UserDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void test1() {
        
    }

}