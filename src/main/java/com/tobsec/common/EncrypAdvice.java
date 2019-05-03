package com.tobsec.common;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SequreField(비밀번호) 애노테이션이 달려있는 필드 암호화
 * 필드의 애노테이션을 검색하는 조인포인트가 스프링에서는 없어서, 사용할 법한 메소드를 가져오는 포인트컷으로 수정
 * {@literal @}Component와 {@literal @}Aspect를 메타애노테이션으로 가지는 애노테이션을 정의하여 사용하려고 했으나, {@literal @}AspectJ 애노테이션이 안 붙어있다는 에러가 발생함
 * 우선 사용하기 편한 Around로 만들었다가, 그 다음에 알맞은 어드바이스로 수정해보기
 */
@Aspect
@Component
public class EncrypAdvice {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Log
    public Logger encrypLogger;

    // 암호화
    @Pointcut("execution(* com.tobsec.service.UserService.addUser*(..)) || execution(* com.tobsec.service.UserService.updateUser(..))")
    public void passwordEncodeService() {} 

    // 복호화
    @Pointcut("execution(* com.tobsec.service.UserService.get*(..)) || execution(* com.tobsec.service.UserService.select*(..))")
    public void passwordDecodeService() {} 

    /*
    // Around 어드바이스 적용 한 것
    @Around("passwordUseService()")
    public Object passwordEncryp(ProceedingJoinPoint joinPoint) throws Throwable {
        encrypLogger.info("Password Use PointCut");
        
        if( joinPoint.getArgs().length > 0 ) {
            Object[] objArr = joinPoint.getArgs();
            for( int i = 0; i < objArr.length; i++ ) {
                final Object obj = objArr[i];
                
                ReflectionUtils.doWithLocalFields(obj.getClass(), new ReflectionUtils.FieldCallback() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        // 주어진 필드에 접근 가능하게 만듦
                        ReflectionUtils.makeAccessible(field);

                        // 필드가 @Log 애노테이션을 가지고 있다면
                        if( field.getAnnotation(Password.class) != null ) {
                            String encrypValue = "";

                            encrypLogger.info("@Password 달려 있는 필드 : " + field.getName());
                            
                            encrypLogger.info("암호화 전 값 : " + field.get(obj));

                            // 암호화 로직

                            encrypValue = "암호화(" + field.get(obj) + ")";
                            field.set(obj, encrypValue);
                        }
                    }
                });
            }
        }
        
        // 메소드 호출 전에 수행 되어야 함.
        Object ret = joinPoint.proceed();

        return ret;
    }
    */

    // Before 어드바이스에서 매개변수의 값 바꾸기
    // @Before("passwordEncodeService()")
    public void passwordEncryp(JoinPoint joinPoint) {
        
        if( joinPoint.getArgs().length > 0 ) {
            Object[] objArr = joinPoint.getArgs();
            for( int i = 0; i < objArr.length; i++ ) {
                final Object obj = objArr[i];
                
                ReflectionUtils.doWithLocalFields(obj.getClass(), new ReflectionUtils.FieldCallback() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        // 주어진 필드에 접근 가능하게 만듦
                        ReflectionUtils.makeAccessible(field);

                        // 필드가 @Log 애노테이션을 가지고 있다면
                        if( field.getAnnotation(Password.class) != null ) {

                            encrypLogger.info("암호화 전 값 : " + field.get(obj));

                            // 암호화 로직
                            String encrypValue = passwordEncoder.encode(field.get(obj).toString());

                            field.set(obj, encrypValue);
                            encrypLogger.info("암호화 후 값 : " + encrypValue);
                        }
                    }
                });
            }
        }
    }

    public Object passwordDecode(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object ret = joinPoint.proceed();

        encrypLogger.info("복호화 전 값 : " + ret);

        if( ret instanceof Collection ) {
            encrypLogger.info("해당 값은 Collection 이다.");
            Collection col = (Collection)ret;
            Iterator it = col.iterator();
            
            while(it.hasNext()) {
                final Object itObj = it.next();
                Class itClsx = itObj.getClass();
                
                ReflectionUtils.doWithLocalFields(itClsx, new ReflectionUtils.FieldCallback() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        // 주어진 필드에 접근 가능하게 만듦
                        ReflectionUtils.makeAccessible(field);
    
                        // 필드가 @Log 애노테이션을 가지고 있다면
                        if( field.getAnnotation(Password.class) != null ) {
                            String decodeValue = "";
    
                            // 복호화 로직
    
                            decodeValue = field.get(itObj).toString();
                            decodeValue = decodeValue.substring(decodeValue.indexOf("암호화(") + 4, decodeValue.lastIndexOf(")"));
                            field.set(itObj, decodeValue);
                        }
                    }
                });
            }
        } else {
            ReflectionUtils.doWithLocalFields(ret.getClass(), new ReflectionUtils.FieldCallback() {
                @Override
                @SuppressWarnings("unchecked")
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    // 주어진 필드에 접근 가능하게 만듦
                    ReflectionUtils.makeAccessible(field);

                    // 필드가 @Log 애노테이션을 가지고 있다면
                    if( field.getAnnotation(Password.class) != null ) {
                        String decodeValue = "";

                        // 복호화 로직

                        decodeValue = field.get(ret).toString();
                        decodeValue = decodeValue.substring(decodeValue.indexOf("암호화(") + 4, decodeValue.lastIndexOf(")"));
                        field.set(ret, decodeValue);
                    }
                }
            });
        }

        encrypLogger.info("복호화 후 값 : " + ret);

        return ret;
    }
}