package com.tobsec.common.convert;

import com.tobsec.common.ConnectionBean;
import com.tobsec.common.convert.*;

import java.io.*;

import com.owlike.genson.Genson;

/**
 * genson으로 파싱. 
 * 작은 크기의 역직렬화(JSON =&gt; Object) 속도가 두 번째로 빠름

 */
public class JsonConvert implements Converter {
    
    private final String DEFAULT_CONFIGFILE = "json_config.json";

    private String configFile = DEFAULT_CONFIGFILE;

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public ConnectionBean makeConnBean() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        ConnectionBean connBean = new ConnectionBean();

        Genson genson = new Genson();

        try (InputStream is = classLoader.getResourceAsStream(configFile)) {
            connBean = genson.deserialize(is, ConnectionBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connBean;
    }
}