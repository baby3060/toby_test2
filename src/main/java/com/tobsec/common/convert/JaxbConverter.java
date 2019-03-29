package com.tobsec.common.convert;

import com.tobsec.common.ConnectionBean;

import com.tobsec.common.convert.*;
import com.tobsec.common.convert.jaxb.Config;

import java.io.IOException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class JaxbConverter implements Converter {
    private String DEFAULT_CONFIG_FILE = "config.xml";
    private String configFile = DEFAULT_CONFIG_FILE;

    public void setConfigFile( String configFile ) {
        this.configFile = configFile;
    }
    
    public ConnectionBean makeConnBean() throws Exception {
        String contextPath = Config.class.getPackage().getName();

        ConnectionBean connBean = new ConnectionBean();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            
            ClassLoader classLoader = getClass().getClassLoader();

            // XML을 자바 객체로 전환하기 위한 언마샬러
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Config config = (Config)unmarshaller.unmarshal(classLoader.getResourceAsStream(this.configFile));

            connBean = new ConnectionBean(config.getClassName(), config.getDatabaseName(), config.getHost(), config.getUserName(), config.getUserPass());

        } catch(JAXBException e) {
            throw new RuntimeException(e);
        }

        return connBean;
    }

}