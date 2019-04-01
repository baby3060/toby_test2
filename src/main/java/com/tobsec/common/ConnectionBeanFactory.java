package com.tobsec.common;

import com.tobsec.common.convert.*;
import org.springframework.beans.factory.FactoryBean;

/**
 * ConnectionBean을 생성하는 팩토리
 */
public class ConnectionBeanFactory implements FactoryBean<ConnectionBean> {
    private Converter converter = new JsonConvert();

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public ConnectionBean getObject() {
        ConnectionBean connBean = new ConnectionBean();

        try {
            connBean = converter.makeConnBean();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return connBean;
    }

    public Class<?> getObjectType() {
        return ConnectionBean.class;
    }

    public boolean isSingleton() {
        return true;
    }
}