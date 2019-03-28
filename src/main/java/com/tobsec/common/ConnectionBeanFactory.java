package com.tobsec.common;

import org.springframework.beans.factory.FactoryBean;

/**
 * ConnectionBean을 생성하는 팩토리
 */
public class ConnectionBeanFactory implements FactoryBean<ConnectionBean> {
    public ConnectionBean getObject() {
        ConnectionBean connBean = new ConnectionBean();

        connBean.setClassName("test");
        
        return connBean;
    }

    public Class<?> getObjectType() {
        return ConnectionBean.class;
    }

    public boolean isSingleton() {
        return true;
    }
}