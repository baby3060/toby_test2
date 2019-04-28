package com.tobsec.common;

import com.tobsec.model.User;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;

import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.Expression;

import org.slf4j.Logger;

/**
 * SecureAccess 애노테이션에 조건 주기
 */
@Aspect
public class AccessAdvice {
    @Log
    public Logger accessLogger;

    @Around("@annotation(sa)")
    public Object secureAdvice(ProceedingJoinPoint joinPoint, SecureAccess sa) throws Throwable {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(sa.value());

        User user = (User)joinPoint.getArgs()[0];

        Boolean result = exp.getValue(user, Boolean.class);

        accessLogger.info(user + "(" + sa.value() + ") result : " + result);
        
        if( result ) {
            Object obj = joinPoint.proceed();
            return obj;
        } else {
            throw new RuntimeException("해당 기능을 사용할 수 없는 user입니다(" + user.getId() + ", " + user.getLevel() + ").");
        }
    }
}