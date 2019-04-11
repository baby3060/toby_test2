package com.tobsec;

import com.tobsec.context.AppConfig;
import com.tobsec.service.sql.SqlService;

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
public class SqlServiceTest {
    @Autowired
    private SqlService sqlService;

    @Before
    public void setUp() { }

    @Test
    public void getSqlTest() throws Exception {
        String addUserSql = sqlService.findSql("user", "addUser");
        String updateUserSql = sqlService.findSql("user", "updateUser");
        String deleteUserSql = sqlService.findSql("user", "deleteUser");

        assertThat(addUserSql, is("Insert Into USER"));
        assertThat(updateUserSql, is("Update USER"));
        assertThat(deleteUserSql, is("Delete From USER"));

        String addConfirmSql = sqlService.findSql("confirm", "addConfirm");
        String deleteConfirmSql = sqlService.findSql("confirm", "deleteConfirm");

        assertThat(addConfirmSql, is("Insert Into CONFIRM"));
        assertThat(deleteConfirmSql, is("Delete From CONFIRM"));
    }

    
}