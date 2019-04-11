package com.tobsec.service.sql;
/**
 * 파일로부터 SQL을 읽어옴.
 */
public interface SqlReader {
    // 읽어옴과 동시에 registry에 저장하기 위해
    public void readSql(SqlRegistry registry);
}