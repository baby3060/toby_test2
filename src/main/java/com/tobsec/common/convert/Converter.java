package com.tobsec.common.convert;

import com.tobsec.common.ConnectionBean;

public interface Converter {
    void setConfigFile(String configFile);
    ConnectionBean makeConnBean() throws Exception;
}