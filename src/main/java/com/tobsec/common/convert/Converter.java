package com.tobsec.common.convert;

import com.tobsec.common.ConnectionBean;

public interface Converter {
    public void setConfigFile(String fileName);
    public ConnectionBean makeConnBean() throws Exception;
}