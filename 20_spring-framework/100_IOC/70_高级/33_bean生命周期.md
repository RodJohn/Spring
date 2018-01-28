

Spring beans的生命周期


在Spring beans生命周期中，提供了一些列的机制来供我们控制这些bean，接下来就从几个方面来看看这些机制都是什么。

InitializingBean 和 DisposableBean 接口


在bean的定义中自定义init和destroy方法

通过注解@PostConstruct和@PreDestroy

其实在整个生命周期中，修改bean的方式大致有如下几种：


实现 DisposableBean接口的 destroy()

给方法加@PostConstruct和@PreDestroy注解，需要说明一点，这两个注解是JSR250标准中的，而Spring中InitDestroyAnnotationBeanPostProcessor类实现了这个标准

以xml的方式定义bean时，指定init method和destroy method




# 参考  initializeBean


```
protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd) {  
        if (System.getSecurityManager() != null) {  
            AccessController.doPrivileged(new PrivilegedAction<Object>() {  
                @Override  
                public Object run() {  
                    invokeAwareMethods(beanName, bean);  
                    return null;  
                }  
            }, getAccessControlContext());  
        }  
        else {  
            invokeAwareMethods(beanName, bean);  
        }  
  
        Object wrappedBean = bean;  
        if (mbd == null || !mbd.isSynthetic()) {  
            //执行BeanPostProcessor扩展点的PostProcessBeforeInitialization进行修改实例化Bean  
            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);  
        }  
  
        try {  
            //调用Bean的初始化方法,这个初始化方法是在BeanDefinition中通过定义init-method属性指定的  
            //同时,如果Bean实现了InitializingBean接口,那么这个Bean的afterPropertiesSet实现也不会被调用  
            invokeInitMethods(beanName, wrappedBean, mbd);  
        }  
        catch (Throwable ex) {  
            throw new BeanCreationException(  
                    (mbd != null ? mbd.getResourceDescription() : null),  
                    beanName, "Invocation of init method failed", ex);  
        }  
  
        if (mbd == null || !mbd.isSynthetic()) {  
            //执行BeanPostProcessor扩展点的PostProcessAfterInitialization进行修改实例化Bean  
            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);  
        }  
        return wrappedBean;  
    }  
```



# InitializingBean

真正的初始化方法invokeInitMethods

```
protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)  
            throws Throwable {  
  
        boolean isInitializingBean = (bean instanceof InitializingBean);  
        if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {  
            if (logger.isDebugEnabled()) {  
                logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");  
            }  
            if (System.getSecurityManager() != null) {  
                try {  
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {  
                        @Override  
                        public Object run() throws Exception {  
                            ((InitializingBean) bean).afterPropertiesSet();  
                            return null;  
                        }  
                    }, getAccessControlContext());  
                }  
                catch (PrivilegedActionException pae) {  
                    throw pae.getException();  
                }  
            }  
            else {  
                ((InitializingBean) bean).afterPropertiesSet(); //启动afterPropertiesSet,afterPropertiesSet是InitializingBean接口的方法  
            }  
        }  
  
        if (mbd != null) {  
            String initMethodName = mbd.getInitMethodName();    //获取用户自定义的初始化方法  
            if (initMethodName != null && !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&  
                    !mbd.isExternallyManagedInitMethod(initMethodName)) {  
                invokeCustomInitMethod(beanName, bean, mbd);    //调用自定义的初始化方法  
            }  
        }  
```



如果该Bean实现了InitializeBean接口，即bean instanceof InitializingBean == true，
就会调用该Bean实现的Initializebean接口的afterPropertiesSet方法



这个方法用来完成一些特殊的初始化操作，并且是在属性都设置好后才会被beanFactory调用。

InitializingBean.afterPropertiesSet()方法

也就是说, 在Spring容器启动过程中，beanFactory的实现类通过initializeBean方法对每个实现的InitializingBean的bean调用其afterPropertiesSet方法。

这就是InitializingBean的机制。


https://mp.weixin.qq.com/s/XZV80ZB6FhSTIubrDmuZHQ



# BeanNameAware，BeanClassLoaderAware，BeanFactoryAware 及ApplicationContextAware接口

作用

方便从上下文中获取当前的运行环境。


## 原理

initializebean方法中有一个invokeAwareMethods方法

```
private void invokeAwareMethods(final String beanName, final Object bean) {  
        if (bean instanceof Aware) {  
            if (bean instanceof BeanNameAware) {  
                ((BeanNameAware) bean).setBeanName(beanName);  
            }  
            if (bean instanceof BeanClassLoaderAware) {  
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());  
            }  
            if (bean instanceof BeanFactoryAware) {  
                ((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);  
            }  
        }  
    } 
```

举例来说，当应用自定义的Bean实现了BeanNameAware接口，如下：

```
public class MyBean implements BeanNameAware {  
    private String beanName;  
  
    void setBeanName(String name) {  
        this.beanName = name;  
    }  
  
}  
```

这样就可以获取到该Bean在Spring容器中的名字，原理就是上述invokeAwareMethods方法中，
判断了如果bean实现了BeanNameAware接口，就会调用该Bean覆盖的BeanNameAware接口的setBeanName方法，
这样MyBean中就获取到了该Bean在Spring容器中的名字。
BeanClassLoaderAware接口和BeanFactoryAware接口同理，可以分别获取Bean的类装载器和bean工厂



# BeanPostProcessor


    是容器级别的修改


```
public interface BeanPostProcessor {
    //bean初始化方法调用前被调用
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
    //bean初始化方法调用后被调用
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}

```


BeanPostProcessor的实现类注册到Spring IOC容器后，
该Spring IOC容器所创建的每个bean实例在初始化方法调用前，
将会调用BeanPostProcessor中的postProcessBeforeInitialization方法，
而在bean实例初始化方法调用完成后，则会调用BeanPostProcessor中的postProcessAfterInitialization方法，




## 参考


常用bean修改

https://mp.weixin.qq.com/s/H-M-J4N8Uyx9SyNT9x7A8A

实现一个log

http://blog.csdn.net/shan9liang/article/details/34421141


常用aware

http://blog.csdn.net/u011734144/article/details/52505263

http://www.cnblogs.com/RunForLove/p/5828570.html


http://blog.csdn.net/miqi770/article/details/51596537


http://blog.csdn.net/songyang19871115/article/details/54342242