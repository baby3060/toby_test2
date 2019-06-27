package com.tobsec.common;

import org.slf4j.Logger;

import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.persistence.*;

@Aspect
@Component
public class EntityManagerAdvice {
    @PersistenceContext
    private EntityManager em;

    @Log
    public Logger entityLogger;

    // @Pointcut("execution(* com.tobsec.dao.*Jpa.delete*(..))")
    @Pointcut("@annotation(com.tobsec.common.JpaTransaction)")
    public void daoPointcut(){}

    @Before("daoPointcut()")
    public void jpaDaoCalled(JoinPoint joinPoint) {
        em.joinTransaction();
    }

    @After("daoPointcut()")
    public void afterDaoCalled() {
        em.flush();
    }

}