package com.tobsec.common;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

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