package com.tobsec;

import com.tobsec.context.*;
import com.tobsec.common.*;

import com.tobsec.common.convert.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

import java.sql.Connection;

public class App {

    public static void main( String[] args ) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        ConnectionBean connBean = ctx.getBean("connBeanFactory", ConnectionBean.class);

        DataSource dataSource = ctx.getBean("dataSource", DataSource.class);

        System.out.println(dataSource == null);

        
    }
}
