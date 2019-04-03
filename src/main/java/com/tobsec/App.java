package com.tobsec;

import java.util.*;

import com.tobsec.context.*;
import com.tobsec.common.*;
import com.tobsec.model.*;

import com.tobsec.dao.*;

import com.tobsec.common.convert.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

import java.sql.Connection;

public class App {

    public static void main( String[] args ) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        ConfirmDao confirmDao = ctx.getBean("confirmDao", ConfirmDao.class);        

    }
}
