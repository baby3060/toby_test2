package com.tobsec.common;

import org.slf4j.Logger;

import com.tobsec.service.exception.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import org.aspectj.lang.annotation.After;

import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {
    @Log
    public Logger aspectLogger;

    @Pointcut("bean(*Service)")
    private void buisinessService(){}

    @Before("buisinessService()")
    public void startServiceLog(JoinPoint joinPoint) {
        Signature sig = joinPoint.getSignature();

        aspectLogger.info("시작할 메소드명 : " + sig.getDeclaringTypeName() + ". " + sig.getName());
        
        Object[] signatureArgs = joinPoint.getArgs();

        for(Object signatureArg: signatureArgs) {
            aspectLogger.info("전달 인자 : " + signatureArg);
        }
    }

    @AfterThrowing(pointcut="buisinessService()", throwing="ex")
    public void runtimeResult(JoinPoint joinPoint, RuntimeException ex) {
        Signature sig = joinPoint.getSignature();

        String methodName = sig.getDeclaringTypeName() + ". " + sig.getName();

        aspectLogger.error(methodName + " 실행 중 RuntimeException 예외 발생");
    }
    
    @AfterReturning(pointcut="buisinessService()")
    public void sucessReturn(JoinPoint joinPoint) {
        Signature sig = joinPoint.getSignature();

        String methodName = sig.getDeclaringTypeName() + ". " + sig.getName();

        aspectLogger.error(methodName + " 실행 완료");
    }
    
}