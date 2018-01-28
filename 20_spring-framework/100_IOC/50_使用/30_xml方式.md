

# 概述

在spring容器中声明bean



基于注释的配置：Spring 2.5引入了对基于注释的配置元数据的支持。

基于Java的配置：从Spring 3.0开始，Spring JavaConfig项目提供的许多功能成为核心Spring框架的一部分。
因此，您可以使用Java而不是XML文件来定义应用程序类外部的Bean。
要使用这些新功能，请参阅@Configuration，@Bean，@Import 和@DependsOn注释。


# 定义

<bean id="person"  name="ren" class="com.john.rod.ioc.Person" />

通过bean元素定义Bean,

名称

id为名称,是唯一(推荐)
name为,是不唯一的,不推荐使用(因为)
class为类,如果没有定义id和name,则使用class的全限定名作为名称


# 注入方式


属性注入(setter方法注入)

```
    <bean id="person" class="com.john.rod.ioc.Person" >
        <property name="name" value="lijun"/>
    </bean>
```

spring将调用name的setter方法进行赋值

构造注入

```
    <bean id="person" class="com.john.rod.ioc.Person" >
        <constructor-arg name="name" value="lijun"/>
    </bean>
```


工厂注入

静态工厂

实例工厂


# 注入值

字面量
    value
    
null     
    
其他bean

    refer 

内部bean

集合

## 简化方式  

p名称空间

## 自动装配

自动在容器中寻找相应的bean
比如 byName byType 

# scope

1、singleton（默认模式）：单例，指一个bean容器中只存在一份
  
  2、prototype：每次请求（每次使用）创建新的实例，destroy方式不生效
  
  3、request：每次http请求创建一个实例且仅在当前request内有效
  
  4、session：同上，每次http请求创建，当前session中有效
  
  5、global session：基于portlet的web中有效（portlet定义了global sessio），如果在web中，同session


# 方法注入 

## lookup

## 方法替换


# bean关系

继承 

依赖

引用


# factoryBean 

http://blog.csdn.net/is_zhoufeng/article/details/38422549
http://blog.csdn.net/u013185616/article/details/52335864


<import/>元素来从另一个或多个文件加载bean定义。
位置路径都是相对于导入的定义文件而言的





http://jinnianshilongnian.iteye.com/blog/1413857


# 声明周期


init 方法



https://mp.weixin.qq.com/s/29EaSBRGeCofO94tQOn_JA

