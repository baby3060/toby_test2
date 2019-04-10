/**
 * Setvice로 끝나는 인터페이스의 경과시간을 나타낼 어드바이스(MethodInterceptor 구현하지 않음)
 * 단순한 부가기능이므로 예외를 throws 하지 않고 끝내기.
 * => 예외가 발생할만한 일은 실행한 메소드에서 발생하는 것이므로 throws 있는 게 좋음
 */
package com.tobsec.service;

import java.util.Arrays;

import com.tobsec.common.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;

public class DuringTimeAdvice {

    @Log
    private Logger logger;

    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long finishTime = System.currentTimeMillis();
            Signature sig = joinPoint.getSignature();
            String format = String.format("%s.%s(%s) 실행 시간 : %d ms\n", 
                joinPoint.getTarget().getClass().getSimpleName()
                , sig.getName()
                , Arrays.toString(joinPoint.getArgs())
                , (finishTime - startTime)
            );
            logger.info(format);
        }
    }
}