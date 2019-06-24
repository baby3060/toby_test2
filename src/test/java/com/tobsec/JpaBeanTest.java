package com.tobsec;

import com.tobsec.common.Log;
import org.slf4j.Logger;

import com.tobsec.context.AppConfig;

import javax.persistence.*;

import java.util.*;
import com.tobsec.model.User;
import com.tobsec.model.Level;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class JpaBeanTest implements ParentTest {
    @Autowired
    private EntityManagerFactory emf;

    @Log
    protected Logger logger;

    @Test
    public void createTest() {
        EntityManager em = emf.createEntityManager();
        
        assertThat(em, is(not(nullValue())));

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.createNativeQuery("Delete From Board").executeUpdate();
            em.createNativeQuery("Delete From Confirm").executeUpdate();
            em.createNativeQuery("Delete From User").executeUpdate();

            em.flush();

            List<User> list = new ArrayList<User>(Arrays.asList(
                new User("1", "사용자1", "1", Level.BRONZE, 0, 0, "a@a.com"),
                new User("2", "사용자2", "2", Level.BRONZE, 0, 0, "b@b.com"),
                new User("3", "사용자3", "3", Level.BRONZE, 0, 0, "c@c.com"),
                new User("4", "사용자4", "4", Level.BRONZE, 0, 0, "d@d.com"),
                new User("5", "사용자5", "5", Level.BRONZE, 0, 0, "e@e.com"),
                new User("6", "사용자6", "6", Level.BRONZE, 0, 0, "f@f.com")
            ));

            long count = em.createQuery("Select Count(u) From User u", Long.class).getSingleResult();

            assertThat(count, is(0L));

            list.stream()
                .forEach(member -> {
                    em.persist(member);
                });
            
            em.flush();
            
            count = em.createQuery("Select Count(u) From User u", Long.class).getSingleResult();

            assertThat(count, is(6L));

            List<User> findList = em.createQuery("Select u From User u Order By u.id", User.class).getResultList();

            assertThat(findList.size(), is(list.size()));

            User listObj = null;
            User findListObj = null;

            for( int i = 0; i < findList.size(); i++ ) {
                listObj = list.get(i);
                findListObj = findList.get(i);
                equalsUser(listObj, findListObj);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void equalsUser(User expected1, User expected2) {
        assertThat(expected1.getId(), is(expected2.getId()));
        assertThat(expected1.getName(), is(expected2.getName()));
        // Service를 타지 않으므로, 비밀번호가 변환되지 않음
        assertThat(expected1.getPassword(), is(expected2.getPassword()));
        assertThat(expected1.getLevel(), is(expected2.getLevel()));
        assertThat(expected1.getLogin(), is(0));
        assertThat(expected1.getRecommend(), is(0));
        assertThat(expected2.getLogin(), is(0));
        assertThat(expected2.getRecommend(), is(0));
        assertThat(expected1.getEmail(), is(expected2.getEmail()));
    }

}