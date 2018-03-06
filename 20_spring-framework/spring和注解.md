


aop 和 自定义注解 

http://blog.csdn.net/fighterandknight/article/details/51170746


https://www.jianshu.com/p/7c2948f64b1c

http://blog.csdn.net/chjttony/article/details/6301523


ComponentScan

   在xml配置了这个标签后，spring可以自动去扫描base-pack下面或者子包下面的java文件，如果扫描到有@Component @Controller@Service等这些注解的类，则把这些类注册为bean

注意：如果配置了<context:component-scan>那么<context:annotation-config/>标签就可以不用再xml中配置了，因为前者包含了后者。另外<context:component-scan>还提供了两个子标签

1.        <context:include-filter>

2.       <context:exclude-filter>

在说明这两个子标签前，先说一下<context:component-scan>有一个use-default-filters属性，改属性默认为true,这就意味着会扫描指定包下的全部的标有@Component的类，并注册成bean.也就是@Component的子注解@Service,@Reposity等。所以如果仅仅是在配置文件中这么写

<context:component-scan base-package="tv.huan.weisp.web"/>

 Use-default-filter此时为true那么会对base-package包或者子包下的所有的进行java类进行扫描,并把匹配的java类注册成bean。

 

 可以发现这种扫描的粒度有点太大，如果你只想扫描指定包下面的Controller，该怎么办？此时子标签<context:incluce-filter>就起到了勇武之地。如下所示

<context:component-scan base-package="tv.huan.weisp.web .controller">  

<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>   

</context:component-scan>  

这样就会只扫描base-package指定下的有@Controller下的java类，并注册成bean

但是因为use-dafault-filter在上面并没有指定，默认就为true，所以当把上面的配置改成如下所示的时候，就会产生与你期望相悖的结果（注意base-package包值得变化）

<context:component-scan base-package="tv.huan.weisp.web">  

<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>   

</context:component-scan>  

此时，spring不仅扫描了@Controller，还扫描了指定包所在的子包service包下注解@Service的java类

此时指定的include-filter没有起到作用，只要把use-default-filter设置成false就可以了。这样就可以避免在base-packeage配置多个包名这种不是很优雅的方法来解决这个问题了。

另外在我参与的项目中可以发现在base-package指定的包中有的子包是不含有注解了，所以不用扫描，此时可以指定<context:exclude-filter>来进行过滤，说明此包不需要被扫描。综合以上说明

Use-dafault-filters=”false”的情况下：<context:exclude-filter>指定的不扫描，<context:include-filter>指定的扫描



Component

被注解的类会自动的被Spring容器注册为BeanDefinition

@Component  
public class UserServiceIml1 implements UserService{  
	***
}  

自动启用了注解的相关功能
<context:component-scan base-package="com.test"/>  

基于@Componet及其扩展（如@Servic和自定义等）标注和classpath-scan定义的Bean，注解有一个value属性，如果提供了，那么就此Bean的名字。如果不提供。就会使用Spring默认的命名机制，即简单类名且第一个字母小写

以上注解表示自动扫描com.test包及其子包下被@component（或者其扩展）表中的类，并把他们注册为bean

是一个元注解，意思是它可以用于标注其他注解，被它标注的注解和它起到相同或者类似的作用。Spring用它定义了其他具有特定意义的注解如@Controller @Service @Repository。


Bean
在采用XML配置bean的时候，我们可以使用实例工厂来定义一个Bean，采用@Bean注解我们也可以做到类似的形式。在一个Bean中定义另外一个Bean。这通过在@Component的标注类中对某个方法使用@Bean进行注解。

@Bean(name="getService")  
   @Qualifier("getService")  
   public UserService getService(@Qualifier("userDao") UserDao user){  
      UserService ser = new UserServiceIml1();  
       
      return ser;  
} 

上述定义一个Bean，并定义了Name和Qualifier属性。还可以定义Scope，Lazy等属性。见下个小节。
其实@Bean更多的是与@Confuguration一起使用，来构建另外一种不同于基于XML的ApplicationContext，即基于注解的，AnnotationConfigApplicationContext。


Configuration
Spring提供一种基于注解的applicationContext，实际应用中，它可以和XML的配置联合使用或者各自单独使用。当使用时，需要很大的利用的@Bean注解。
@Configuration  
public class MyAnnoConfig {  
   
   @Bean(name="cDao1")  
   public UserDao getConfigDao(){  
      return new UserDaoImp();  
   }  
   
    
} 

CommandLineRunner 

Spring Boot应用程序在启动后，会遍历CommandLineRunner接口的实例并运行它们的run方法。
也可以利用@Order注解（或者实现Order接口）来规定所有CommandLineRunner实例的运行顺序。


import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
 
@Component
@Order(value=2)
public class MyStartupRunner1 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作 11111111 <<<<<<<<<<<<<");
    }
}



ApplicationContextAware


在ApplicationContextAware的实现类中，可以获得ApplicationContex,也就可以通过这个上下文环境对象得到Spring容器中的Bean。


import org.springframework.beans.BeansException;  
import org.springframework.context.ApplicationContext;  
import org.springframework.context.ApplicationContextAware;  
 
@Component 
public class SpringContextHelper implements ApplicationContextAware {  
    private static ApplicationContext context = null;  
  
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext)  
            throws BeansException {  
        context = applicationContext;  
    }  
      
    public static Object getBean(String name){  
        return context.getBean(name);  
    }  
      
}  



参考
http://blog.csdn.net/windsunmoon/article/details/44498169



