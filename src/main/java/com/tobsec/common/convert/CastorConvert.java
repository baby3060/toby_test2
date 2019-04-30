package com.tobsec.common.convert;

import com.tobsec.common.ConnectionBean;

import com.tobsec.common.convert.jaxb.Config;

import org.exolab.castor.mapping.Mapping; 
import org.exolab.castor.xml.Unmarshaller;

import java.io.*;

public class CastorConvert implements Converter {
    private String DEFAULT_CONFIG_FILE = "config.xml";
    private String configFile = DEFAULT_CONFIG_FILE;

    public void setConfigFile( String configFile ) {
        this.configFile = configFile;
    }
    
    public ConnectionBean makeConnBean() throws RuntimeException {
        
        ConnectionBean connBean = new ConnectionBean();

        ClassLoader classLoader = getClass().getClassLoader();

        Reader reader = null;
        Mapping mapping = new Mapping();
        try {
            File file = new File(classLoader.getResource(this.configFile).getFile());
            reader = new FileReader(file);

            mapping.loadMapping(classLoader.getResource("castor/mapping.xml"));

            Unmarshaller unmarshaller = new Unmarshaller(mapping);
            unmarshaller.setClass(Config.class);
            
            Config config = (Config)unmarshaller.unmarshal(reader);
            
            connBean = new ConnectionBean(config.getClassName(), config.getDatabaseName(), config.getHost(), config.getUserName(), config.getUserPass());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return connBean;
    }
}