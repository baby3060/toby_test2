package com.tobsec.service.sql;

import java.util.Map;
import com.tobsec.service.sql.exception.*;

/**
 * 읽어들인 SQL을 자체에서 저장함.
 * SqlService에서 이 인터페이스의 메소드를 호출하여 가져감
 */
public interface SqlRegistry {
    void registSql(String gubun, Map<String, String> sqlDtl);
    String getSql(String gubun, String key) throws SqlRetriveFailException;
}