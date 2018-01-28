package com.john.rod.ioc.pojo;

public class Dog implements Pet {
    @Override
    public void say() {
        System.out.println( "wang wang wang ");
    }
}
