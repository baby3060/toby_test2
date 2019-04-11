package com.tobsec.service.sql;

public interface SqlService {
    /**
     * SqlService에서 정의한 SqlRegistry의 getSql 메소드 호출
     */
    public String findSql(String gubun, String key);
}