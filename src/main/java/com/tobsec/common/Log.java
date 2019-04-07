package com.tobsec.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Retention : 애노테이션 범위
 * @Documented : 자바 문서에도 애노테이션 보임
 * @Target : 애노테이션이 적용될 위치
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Log {}