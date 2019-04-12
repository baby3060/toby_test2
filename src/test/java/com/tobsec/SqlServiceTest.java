package com.tobsec;

import com.tobsec.context.AppConfig;
import com.tobsec.service.sql.SqlService;

import java.util.*;
import java.util.Map.Entry;

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

    private Map<String, Map<String, String>> sqlMap;

    private List<String> gubunList;

    @Before
    public void setUp() { 
        gubunList = new ArrayList<String>();

        sqlMap = new HashMap<String, Map<String, String>>();

        String gubun1 = "user", gubun2 = "confirm";

        gubunList.add(gubun1);
        gubunList.add(gubun2);

        Map<String, String> keyMap = new HashMap<String, String>();

        keyMap.put("addUser", "Add User");
        keyMap.put("updateUser", "Update User");
        keyMap.put("deleteUser", "Delete User");

        sqlMap.put(gubun1, keyMap);

        keyMap = new HashMap<String, String>();

        keyMap.put("addConfirm", "Add Confirm");
        keyMap.put("updateConfirm", "Update Confirm");
        keyMap.put("deleteConfirm", "Delete Confirm");

        sqlMap.put(gubun2, keyMap);
    }

    @Test
    public void getSqlTest() throws Exception {
        String addUserSql = sqlService.findSql("user", "addUser");
        String updateUserSql = sqlService.findSql("user", "updateUser");
        String deleteUserSql = sqlService.findSql("user", "deleteUser");

        assertThat(addUserSql, is("Insert Into User (id, name, password, level, login, recommend, email, recid) Values (:id, :name, :password, :level, :login, :recommend, :email, :recid)"));
        assertThat(updateUserSql, is("Update User Set name = :name, password = :password, email = :email Where id = :id"));
        assertThat(deleteUserSql, is("Delete From User Where id = :id"));

        String addConfirmSql = sqlService.findSql("confirm", "addConfirm");
        String deleteConfirmSql = sqlService.findSql("confirm", "deleteConfirm");

        assertThat(addConfirmSql, is("Insert Into CONFIRM(id, confirm_date, confirm_seq, confirm_time, content) Values (:id, :confirm_date, :confirm_seq, (Select DATE_FORMAT(NOW(),'%H%i%s')), :content)"));
        assertThat(deleteConfirmSql, is("Delete From CONFIRM Where id = :id And confirm_date = :confirm_date And confirm_seq = :confirm_seq"));
    }

    @Test
    public void testEntry() {
        TreeMap<String, Map<String, String>> sortedMap = new TreeMap<>(sqlMap);

        assertThat(sortedMap.size(), is(2));

        String gubun = "";

        int i = 1;

        Set<Map.Entry<String, Map<String, String>>> entrySetP = sortedMap.entrySet();

        Iterator<Entry<String, Map<String, String>>> iterator = entrySetP.iterator();

        Entry<String, Map<String, String>> entry1 = iterator.next();

        assertThat(entry1.getKey(), is("confirm"));

        Set<Map.Entry<String, String>> entrySetC = entry1.getValue().entrySet();

        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(entrySetC);

        assertThat(list.get(0).getKey(), is("addConfirm"));
        assertThat(list.get(0).getValue(), is("Add Confirm"));

        assertThat(list.get(1).getKey(), is("updateConfirm"));
        assertThat(list.get(1).getValue(), is("Update Confirm"));

        assertThat(list.get(2).getKey(), is("deleteConfirm"));
        assertThat(list.get(2).getValue(), is("Delete Confirm"));

        entry1 = iterator.next();
        assertThat(entry1.getKey(), is("user"));

        entrySetC = entry1.getValue().entrySet();

        list = new ArrayList<Map.Entry<String, String>>(entrySetC);

        assertThat(list.get(0).getKey(), is("addUser"));
        assertThat(list.get(0).getValue(), is("Add User"));

        assertThat(list.get(1).getKey(), is("deleteUser"));
        assertThat(list.get(1).getValue(), is("Delete User"));

        assertThat(list.get(2).getKey(), is("updateUser"));
        assertThat(list.get(2).getValue(), is("Update User"));
    }

    
}