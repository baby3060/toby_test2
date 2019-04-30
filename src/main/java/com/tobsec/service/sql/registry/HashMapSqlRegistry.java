package com.tobsec.service.sql.registry;

import com.tobsec.service.sql.SqlRegistry;

import java.util.Map;
import java.util.HashMap;

import com.tobsec.service.sql.exception.*;

public class HashMapSqlRegistry implements SqlRegistry {
    Map<String, Map<String, String>> sqlMap = new HashMap<String, Map<String, String>>();

    // HashMap에 저장
    public void registSql(String gubun, Map<String, String> sqlDtl) {
        sqlMap.put(gubun, sqlDtl);
    }
    public String getSql(String gubun, String key) throws SqlRetriveFailException {
        Map<String, String> dtlSql = null;

        String sql = "";
        
        if( sqlMap.containsKey(gubun) ) {
            
            dtlSql = sqlMap.get(gubun);

            if(dtlSql.containsKey(key)) {
                sql = dtlSql.get(key);
            } else {
                throw new SqlRetriveFailException("해당 키가 존재하지 않습니다(" + key + ").");
            }

        } else {
            throw new SqlRetriveFailException("해당 구분값은 존재하지 않습니다(" + gubun + ").");
        }

        return sql;
    }
}