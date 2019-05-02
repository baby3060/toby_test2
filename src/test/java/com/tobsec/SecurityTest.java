package com.tobsec;

import com.tobsec.context.SecurityConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import com.tobsec.common.Log;

import org.slf4j.Logger;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SecurityConfig.class)
public class SecurityTest implements ParentTest  {
    @Log
    protected Logger serviceLogger;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void securityTest() {
        String password = "password";
 
        String encPassword = passwordEncoder.encode(password);

        serviceLogger.info("원래 암호 : " + password);
        serviceLogger.info("변환된 암호 : " + encPassword);

        assertThat(encPassword, startsWith("{bcrypt}"));
    }

    @Test
    public void matchTest() {
        String password = "password";
 
        String encPassword = passwordEncoder.encode(password);

        assertTrue(passwordEncoder.matches(password, encPassword));
    }

}
