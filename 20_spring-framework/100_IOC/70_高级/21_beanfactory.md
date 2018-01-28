


# FactoryBean接口
  
       用于创建特定的对象，对象的类型由getObject方法的返回值决定。

```

public interface Animal {
    public abstract void say();
}

public class Cat implements Animal {
    @Override
    public void say() {
        System.out.println("cat.say()");
    }
}

public class Dog implements Animal {
    @Override
    public void say() {
        System.out.println("Dog.say()");
    }
}

```
       
```
@Service
public class FactoryBeanTest implements FactoryBean<Animal> {
    private static String name = "cat"; // 可以在xml中配置
    @Override
    public Animal getObject() throws Exception {
        if("cat".equals(name)){
            return new Cat();
        }else if ("dog".equals(name)) {
            return new Dog();
        }
        return null;
    }
    @Override
    public Class<?> getObjectType() {
        return Animal.class;
    }
    @Override
    public boolean isSingleton() {
        return true;// 返回单例
    }
}
```       

```
public class JunitTest {
    @Test
    public void test() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("config/spring/beans.xml");
        //此处得到的是XX.Cat@57c39581，可见得到了具体的实现类
        Object object = applicationContext.getBean("factoryTest");
        Animal animal = (Animal) object;
        // 结果为：Cat.say()
        animal.say();
    }
}

```



# BeanFactoryPostProcessor接口


1、实现自BeanFactoryPostProcessor，即可以在Spring创建Bean之前，读取Bean的元属性，并根据需要对元属性进行修改，比如将Bean的scope从singleton改变为prototype
2、BeanFactoryPostProcessor的优先级高于BeanPostProcessor
3、BeanFactoryPostProcessor的postProcessorBeanFactory()方法只会执行一次


```
public class BeanFactoryPostProcessorTest implements BeanFactoryPostProcessor {
    /**
     * 获取Bean元属性并修改等操作
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("postProcessBeanFactory(...)");
        
        //修改元属性scope
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("systemParamInit");
        System.out.println("scope for systemParamInit before : " + beanDefinition.getScope());
        beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        System.out.println("scope for systemParamInit after : " + beanDefinition.getScope());
        
        //获取Bean的属性参数并打印
        MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();
        System.out.println("systemParamInit properties :" + mutablePropertyValues);
    }
}
```




    
    
    