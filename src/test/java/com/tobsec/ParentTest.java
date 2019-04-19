package com.tobsec;

import org.springframework.test.context.ActiveProfiles;

/**
 * Profile을 설정하기 위한 테스트 공통 Marker 인터페이스
 */
@ActiveProfiles("first")
public interface ParentTest {}