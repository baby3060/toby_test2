package com.tobsec.common;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogApprovalProcessor implements BeanPostProcessor {
    
    /**
     * Bean 초기화 콜백 이전에 처리
     */
    public Object postProcessBeforeInitialization(final Object target, String beanName) throws BeansException {

        ReflectionUtils.doWithLocalFields(
            target.getClass()
            , new ReflectionUtils.FieldCallback() {
                @Override
                @SuppressWarnings("unchecked")
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    // 주어진 필드에 접근 가능하게 만듦
                    ReflectionUtils.makeAccessible(field);

                    // 필드가 @Log 애노테이션을 가지고 있다면
                    if( field.getAnnotation(Log.class) != null ) {
                        // 대상 클래스의 Logger 생성
                        Logger logger = LoggerFactory.getLogger(target.getClass());

                        // 대상 필드롤 Logger로 설정
                        field.set(target, logger);
                    }
                }
            }
        );
 
        return target;
    }

    /**
     * Bean 초기화 콜백 이후에 반환(래핑할 때 사용하는 게 좋음)
     */
    public Object postProcessAfterInitialization(Object target, String beanName) throws BeansException {
        return target;
    }
}