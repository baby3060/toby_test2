package com.tobsec.service.sql;

import java.util.Map;

import com.tobsec.service.sql.exception.*;

public interface UpdateAbleRegistry extends SqlRegistry {
    public void updateSql(String gubun, String key, String sql) throws SqlUpdateFailureException;
    public void updateSql(Map<String, Map<String, String>> sqlMap) throws SqlUpdateFailureException;
}