package com.tobsec;

import com.tobsec.context.*;
import com.tobsec.common.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App 
{
    public static void main( String[] args ) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // ConnectionBeanFactory에서 생산된 건 ConnectionBean
        ConnectionBean connBean = ctx.getBean("connBeanFactory", ConnectionBean.class);

        System.out.println(connBean.getClassName());
    }
}
