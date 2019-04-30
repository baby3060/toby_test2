package com.tobsec.service.sql;

public interface SqlService {
    /**
     * SqlService에서 정의한 SqlRegistry의 getSql 메소드 호출
     * @param gubun Sql Gubun
     * @param key Gubun의 Key
     * @return 저장된 SQL
     */
    String findSql(String gubun, String key);
}