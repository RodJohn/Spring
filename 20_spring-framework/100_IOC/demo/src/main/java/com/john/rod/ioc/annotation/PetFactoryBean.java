package com.john.rod.ioc.annotation;


import com.john.rod.ioc.pojo.Pet;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class PetFactoryBean<T> implements FactoryBean
        <T>, InitializingBean {

    private Class<T> theInterface;

    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<T> getObjectType() {
        return theInterface ;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
