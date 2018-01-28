package com.john.rod.ioc.annotation;

import com.john.rod.ioc.pojo.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);


        Person person = (Person)ctx.getBean("person");
        System.out.println(person.getName());


    }
}
