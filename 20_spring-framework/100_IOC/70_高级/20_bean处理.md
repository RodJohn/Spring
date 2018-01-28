
initializinngBean、DisposableBean、init-method、destroy-method

# InitializingBean
通过实现InitializingBean接口可以在BeanFactory 设置所有的属性后作出进一步的反应可以实现该接口。


# InitialingBean和DisposableBean 

# BeanPostProcessor


当需要对受管bean进行预处理时，可以新建一个实现BeanPostProcessor接口的类，并将该类配置到Spring容器中。
 实现BeanPostProcessor接口时，需要实现以下两个方法：
      postProcessBeforeInitialization 在受管bean的初始化动作之前调用
      postProcessAfterInitialization 在受管bean的初始化动作之后调用
      
      
```
public class CustomBeanPostProcessor implements BeanPostProcessor {  
    /** 
     * 初始化之前的回调方法 
     */  
    public Object postProcessBeforeInitialization(Object bean, String beanName)throws BeansException {  
        System.out.println("postProcessBeforeInitialization: " + beanName);  
        return bean;  
    }  
  
    /** 
     * 初始化之后的回调方法 
     */  
    public Object postProcessAfterInitialization(Object bean, String beanName)throws BeansException {  
        System.out.println("postProcessAfterInitialization: " + beanName);  
        return bean;  
    }  
}  
```



# InstantiationAwareBeanPostProcessor

    1、InstantiationAwareBeanPostProcessor又代表了另外一个Bean的生命周期：实例化
    2、实例化 --- 实例化的过程是一个创建Bean的过程，即调用Bean的构造函数，单例的Bean放入单例池中
          初始化 --- 初始化的过程是一个赋值的过程，即调用Bean的setter，设置Bean的属性
    3、之前的BeanPostProcessor作用于2过程，现在这个类则作用于1过程
    
```
@Service
public class InstantiationAwareBeanPostProcessorTest implements InstantiationAwareBeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("InstantiationAwareBeanPostProcessorTest.postProcessBeforeInitialization()");
        return bean;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("InstantiationAwareBeanPostProcessorTest.postProcessAfterInitialization()");
        return bean;
    }
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("InstantiationAwareBeanPostProcessorTest.postProcessBeforeInstantiation()");
        return beanClass;
    }
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("InstantiationAwareBeanPostProcessorTest.postProcessAfterInstantiation()");
        return true;
    }
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        return pvs;
    }
}
```    

      