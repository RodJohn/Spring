package com.john.rod.ioc.importBD;

import com.john.rod.ioc.pojo.Person;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

@Component
@Import(MyImportBd.class)
public class MyImportBd implements ImportBeanDefinitionRegistrar {

    public static String personD = "personD";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if(!registry.containsBeanDefinition(personD)){
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(Person.class);
            beanDefinition.setSynthetic(true);
            registry.registerBeanDefinition(personD, beanDefinition);
        }
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyImportBd.class);
        Person person = (Person)ctx.getBean(personD);
        System.out.println(person.getName());
    }
}
