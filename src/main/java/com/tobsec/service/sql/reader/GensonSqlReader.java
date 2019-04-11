package com.tobsec.service.sql.reader;

import com.tobsec.service.sql.SqlReader;
import com.tobsec.service.sql.SqlRegistry;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.owlike.genson.*;

public class GensonSqlReader implements SqlReader {
    private final String DEFAULT_CONFIG_FILE = "sqlconfig/sqlMap.json";
    private String configFile = DEFAULT_CONFIG_FILE;

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @SuppressWarnings("unchecked")
    public void readSql(SqlRegistry registry) {

        ClassLoader classLoader = getClass().getClassLoader();

        Genson genson = new Genson();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File( classLoader.getResource(this.configFile).getFile())),"UTF8"));

            List<Map<String, Object>> sqlMapList = genson.deserialize(br, List.class);

            String gubun = "";
            Map<String, String> keyAndSqlMap = null;

            String key = "", sql = "";

            Map<String, Object> outerMap = null;
            List<Map<String, String>> sqlMap = null;
            Map<String, String> tempInner = null;

            for( int outer = 0; outer < sqlMapList.size(); outer++ ) {
                outerMap = sqlMapList.get(outer);

                gubun = outerMap.get("gubun").toString();
                keyAndSqlMap = new HashMap<String, String>();

                sqlMap = (List<Map<String, String>>)outerMap.get("sqlMap");

                for( int inner = 0; inner < sqlMap.size(); inner++ ) {
                    tempInner = sqlMap.get(inner);

                    key = tempInner.get("key");
                    sql = tempInner.get("sql");

                    keyAndSqlMap.put(key, sql);
                }

                registry.registSql(gubun, keyAndSqlMap);
            }
        } catch(IOException ie) {
            ie.printStackTrace();
        } finally {
            if( br != null ) {
                try {
                    br.close();
                } catch(IOException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}