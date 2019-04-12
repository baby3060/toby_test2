package com.tobsec.service.sql.registry;

import java.util.Map;

import javax.sql.DataSource;

import com.tobsec.service.sql.UpdateAbleRegistry;
import com.tobsec.service.sql.exception.*;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import org.springframework.transaction.TransactionStatus;

import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.annotation.Resource;

public class EmbeddedDbSqlRegstry implements UpdateAbleRegistry {
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    @Resource(name="embeddedDataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    public void registSql(String gubun, Map<String, String> sqlDtl) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                String key = "", sql = "";
                for( Map.Entry<String, String> entryIn : sqlDtl.entrySet() ) {
                    key = entryIn.getKey();
                    sql = entryIn.getValue();

                    registSql(gubun, key, sql);
                }
            }
        });
    }

    private void registSql(String gubun, String key, String sql) {
        this.jdbcTemplate.update("Insert Into sqlmap(gubun_, key_, sql_) Values (?, ?, ?)", new Object[]{gubun, key, sql});
    }

    public String getSql(String gubun, String key) throws SqlRetriveFailException {
        try {
            return this.jdbcTemplate.queryForObject("Select sql_ From sqlmap Where gubun_ = ? And key_ = ? ", new Object[]{gubun, key}, String.class);
        } catch(IncorrectResultSizeDataAccessException e) {
            throw new SqlRetriveFailException("(" + gubun + ", " + key + ")에 해당하는 SQL을 찾을 수 없습니다.");
        }
    }

    public void updateSql(String gubun, String key, String sql) throws SqlUpdateFailureException {
        int affected = this.jdbcTemplate.update("Update sqlmap set sql_ = ? Where gubun = ? And key_ = ?", new Object[]{sql, gubun, key});

        if( affected == 0 ) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        }
    }

    public void updateSql(Map<String, Map<String, String>> sqlMap) throws SqlUpdateFailureException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                String gubun = "";
                Map<String, String> inner = null;

                String key = "", sql = "";

                for( Map.Entry<String, Map<String, String>> entry : sqlMap.entrySet() ) {
                    gubun = entry.getKey();
                    inner = entry.getValue();

                    for( Map.Entry<String, String> entryIn : inner.entrySet() ) {
                        key = entryIn.getKey();
                        sql = entryIn.getValue();

                        updateSql(gubun, key, sql);
                    }
                }
            }
        });
    }
}