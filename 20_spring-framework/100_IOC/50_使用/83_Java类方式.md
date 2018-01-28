
Spring中新的基于Java的配置的核心就是支持@Configuration注解的类以及@Bean注解的方法。


# @Configuration

注解了@Configuration的类就表示这个类的首要目的是用来管理Bean的配置的。

## 使能组件扫描
使能组件扫描在前文中也略有提及，只需要在@Configuration注解的类上配置即可：
@ComponentScan(basePackages = "com.acme")

# @Bean

@Bean注解用来表示一个方法会实例化，配置，并初始化一个新的由Spring IoC容器所管理的对象。其作用等于XML配置中的<beans>标签下的<bean>子标签。

注入inter-bean依赖
当@Bean方法依赖于其他的Bean的时候，可以通过在另一个方法中调用即可。

```
@Configuration
public class AppConfig {

    @Bean
    public Foo foo() {
        return new Foo(bar());
    }

    @Bean
    public Bar bar() {
        return new Bar();
    }
}
```

# @Import

跟XML中通过使用<import/>标签来实现模块化配置类似，
基于Java的配置中也包含@Import注解来加载另一个配置类之中的Bean：

# 示例

```
@Configuration
@ComponentScan(basePackages = "com.acme")
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }

}


上面的配置将等价于如下的XML配置：


<beans>
    <context:component-scan base-package="com.acme"/>
    <bean id="myService" class="com.acme.services.MyServiceImpl"/>
</beans>

```

```
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigB.class);

    // now both beans A and B will be available...
    A a = ctx.getBean(A.class);
    B b = ctx.getBean(B.class);
}
```


# 参考

http://blog.csdn.net/ethanwhite/article/details/52076072