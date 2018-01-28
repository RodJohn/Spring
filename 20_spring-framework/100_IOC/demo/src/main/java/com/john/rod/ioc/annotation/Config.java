package com.john.rod.ioc.annotation;


import com.john.rod.ioc.pojo.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;

public class Config implements BeanPostProcessor {

    @Bean
    Person person(){
        return new Person();
    }

    // Bean 实例化之前执行该方法
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        System.out.println( beanName + "开始实例化");
        return bean;
    }

    // Bean 实例化之后执行该方法
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        System.out.println( beanName + "实例化完成");
        if (bean.getClass() == Person.class){
            Person person = (Person) bean;
            person.setName("lijun");
        }
        return bean;
    }

}
