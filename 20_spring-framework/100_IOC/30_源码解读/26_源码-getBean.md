



BeanFactory

# getBean

Object getBean(String var1) throws BeansException;

# doGetBean

```
//创建Bean实例对象  
   protected Object createBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)  
           throws BeanCreationException {  
       if (logger.isDebugEnabled()) {  
           logger.debug("Creating instance of bean '" + beanName + "'");  
       }  
       //判断需要创建的Bean是否可以实例化，即是否可以通过当前的类加载器加载  
       resolveBeanClass(mbd, beanName);  
       //校验和准备Bean中的方法覆盖  
       try {  
           mbd.prepareMethodOverrides();  
       }  
       catch (BeanDefinitionValidationException ex) {  
           throw new BeanDefinitionStoreException(mbd.getResourceDescription(),  
                   beanName, "Validation of method overrides failed", ex);  
       }  
       try {  
           //如果Bean配置了初始化前和初始化后的处理器，则试图返回一个需要创建//Bean的代理对象  
           Object bean = resolveBeforeInstantiation(beanName, mbd);  
           if (bean != null) {  
               return bean;  
           }  
       }  
       catch (Throwable ex) {  
           throw new BeanCreationException(mbd.getResourceDescription(), beanName,  
                   "BeanPostProcessor before instantiation of bean failed", ex);  
       }  
       //创建Bean的入口  
       Object beanInstance = doCreateBean(beanName, mbd, args);  
       if (logger.isDebugEnabled()) {  
           logger.debug("Finished creating instance of bean '" + beanName + "'");  
       }  
       return beanInstance;  
   }  
   //真正创建Bean的方法  
   protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args) {  
       //封装被创建的Bean对象  
       BeanWrapper instanceWrapper = null;  
       if (mbd.isSingleton()){//单态模式的Bean，先从容器中缓存中获取同名Bean  
           instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);  
       }  
       if (instanceWrapper == null) {  
           //创建实例对象  
           instanceWrapper = createBeanInstance(beanName, mbd, args);  
       }  
       final Object bean = (instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null);  
       //获取实例化对象的类型  
       Class beanType = (instanceWrapper != null ? instanceWrapper.getWrappedClass() : null);  
       //调用PostProcessor后置处理器  
       synchronized (mbd.postProcessingLock) {  
           if (!mbd.postProcessed) {  
               applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);  
               mbd.postProcessed = true;  
           }  
       }  
       // Eagerly cache singletons to be able to resolve circular references  
       //向容器中缓存单态模式的Bean对象，以防循环引用  
       boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&  
               isSingletonCurrentlyInCreation(beanName));  
       if (earlySingletonExposure) {  
           if (logger.isDebugEnabled()) {  
               logger.debug("Eagerly caching bean '" + beanName +  
                       "' to allow for resolving potential circular references");  
           }  
           //这里是一个匿名内部类，为了防止循环引用，尽早持有对象的引用  
           addSingletonFactory(beanName, new ObjectFactory() {  
               public Object getObject() throws BeansException {  
                   return getEarlyBeanReference(beanName, mbd, bean);  
               }  
           });  
       }  
       //Bean对象的初始化，依赖注入在此触发  
       //这个exposedObject在初始化完成之后返回作为依赖注入完成后的Bean  
       Object exposedObject = bean;  
       try {  
           //将Bean实例对象封装，并且Bean定义中配置的属性值赋值给实例对象  
           populateBean(beanName, mbd, instanceWrapper);  
           if (exposedObject != null) {  
               //初始化Bean对象  
               exposedObject = initializeBean(beanName, exposedObject, mbd);  
           }  
       }  
       catch (Throwable ex) {  
           if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {  
               throw (BeanCreationException) ex;  
           }  
           else {  
               throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);  
           }  
       }  
       if (earlySingletonExposure) {  
           //获取指定名称的已注册的单态模式Bean对象  
           Object earlySingletonReference = getSingleton(beanName, false);  
           if (earlySingletonReference != null) {  
               //根据名称获取的以注册的Bean和正在实例化的Bean是同一个  
               if (exposedObject == bean) {  
                   //当前实例化的Bean初始化完成  
                   exposedObject = earlySingletonReference;  
               }  
               //当前Bean依赖其他Bean，并且当发生循环引用时不允许新创建实例对象  
               else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {  
                   String[] dependentBeans = getDependentBeans(beanName);  
                   Set<String> actualDependentBeans = new LinkedHashSet<String>(dependentBeans.length);  
                   //获取当前Bean所依赖的其他Bean  
                   for (String dependentBean : dependentBeans) {  
                       //对依赖Bean进行类型检查  
                       if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {  
                           actualDependentBeans.add(dependentBean);  
                       }  
                   }  
                   if (!actualDependentBeans.isEmpty()) {  
                       throw new BeanCurrentlyInCreationException(beanName,  
                               "Bean with name '" + beanName + "' has been injected into other beans [" +  
                               StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +  
                               "] in its raw version as part of a circular reference, but has eventually been " +  
                               "wrapped. This means that said other beans do not use the final version of the " +  
                               "bean. This is often the result of over-eager type matching - consider using " +  
                               "'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");  
                   }  
               }  
           }  
       }  
       //注册完成依赖注入的Bean  
       try {  
           registerDisposableBeanIfNecessary(beanName, bean, mbd);  
       }  
       catch (BeanDefinitionValidationException ex) {  
           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);  
       }  
       return exposedObject;  
    }

```

如果Bean定义的单态模式(Singleton)，则容器在创建之前先从缓存中查找，以确保整个容器中只存在一个实例对象。
如果Bean定义的是原型模式(Prototype)，则容器每次都会创建一个新的实例对象。除此之外，Bean定义还可以扩展为指定其生命周期范围。


# createBean


```
   protected Object createBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)  
           throws BeanCreationException {  
       if (logger.isDebugEnabled()) {  
           logger.debug("Creating instance of bean '" + beanName + "'");  
       }  
       //判断需要创建的Bean是否可以实例化，即是否可以通过当前的类加载器加载  
       resolveBeanClass(mbd, beanName);  
       //校验和准备Bean中的方法覆盖  
       try {  
           mbd.prepareMethodOverrides();  
       }  
       catch (BeanDefinitionValidationException ex) {  
           throw new BeanDefinitionStoreException(mbd.getResourceDescription(),  
                   beanName, "Validation of method overrides failed", ex);  
       }  
       try {  
           //如果Bean配置了初始化前和初始化后的处理器，则试图返回一个需要创建//Bean的代理对象  
           Object bean = resolveBeforeInstantiation(beanName, mbd);  
           if (bean != null) {  
               return bean;  
           }  
       }  
       catch (Throwable ex) {  
           throw new BeanCreationException(mbd.getResourceDescription(), beanName,  
                   "BeanPostProcessor before instantiation of bean failed", ex);  
       }  
       //创建Bean的入口  
       Object beanInstance = doCreateBean(beanName, mbd, args);  
       if (logger.isDebugEnabled()) {  
           logger.debug("Finished creating instance of bean '" + beanName + "'");  
       }  
       return beanInstance;  
   }  
```


# doCreateBean


```
   protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args) {  
       //封装被创建的Bean对象  
       BeanWrapper instanceWrapper = null;  
       if (mbd.isSingleton()){//单态模式的Bean，先从容器中缓存中获取同名Bean  
           instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);  
       }  
       if (instanceWrapper == null) {  
           //创建实例对象  
           instanceWrapper = createBeanInstance(beanName, mbd, args);  
       }  
       final Object bean = (instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null);  
       //获取实例化对象的类型  
       Class beanType = (instanceWrapper != null ? instanceWrapper.getWrappedClass() : null);  
       //调用PostProcessor后置处理器  
       synchronized (mbd.postProcessingLock) {  
           if (!mbd.postProcessed) {  
               applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);  
               mbd.postProcessed = true;  
           }  
       }  
       // Eagerly cache singletons to be able to resolve circular references  
       //向容器中缓存单态模式的Bean对象，以防循环引用  
       boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&  
               isSingletonCurrentlyInCreation(beanName));  
       if (earlySingletonExposure) {  
           if (logger.isDebugEnabled()) {  
               logger.debug("Eagerly caching bean '" + beanName +  
                       "' to allow for resolving potential circular references");  
           }  
           //这里是一个匿名内部类，为了防止循环引用，尽早持有对象的引用  
           addSingletonFactory(beanName, new ObjectFactory() {  
               public Object getObject() throws BeansException {  
                   return getEarlyBeanReference(beanName, mbd, bean);  
               }  
           });  
       }  
       //Bean对象的初始化，依赖注入在此触发  
       //这个exposedObject在初始化完成之后返回作为依赖注入完成后的Bean  
       Object exposedObject = bean;  
       try {  
           //将Bean实例对象封装，并且Bean定义中配置的属性值赋值给实例对象  
           populateBean(beanName, mbd, instanceWrapper);  
           if (exposedObject != null) {  
               //初始化Bean对象  
               exposedObject = initializeBean(beanName, exposedObject, mbd);  
           }  
       }  
       catch (Throwable ex) {  
           if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {  
               throw (BeanCreationException) ex;  
           }  
           else {  
               throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);  
           }  
       }  
       if (earlySingletonExposure) {  
           //获取指定名称的已注册的单态模式Bean对象  
           Object earlySingletonReference = getSingleton(beanName, false);  
           if (earlySingletonReference != null) {  
               //根据名称获取的以注册的Bean和正在实例化的Bean是同一个  
               if (exposedObject == bean) {  
                   //当前实例化的Bean初始化完成  
                   exposedObject = earlySingletonReference;  
               }  
               //当前Bean依赖其他Bean，并且当发生循环引用时不允许新创建实例对象  
               else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {  
                   String[] dependentBeans = getDependentBeans(beanName);  
                   Set<String> actualDependentBeans = new LinkedHashSet<String>(dependentBeans.length);  
                   //获取当前Bean所依赖的其他Bean  
                   for (String dependentBean : dependentBeans) {  
                       //对依赖Bean进行类型检查  
                       if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {  
                           actualDependentBeans.add(dependentBean);  
                       }  
                   }  
                   if (!actualDependentBeans.isEmpty()) {  
                       throw new BeanCurrentlyInCreationException(beanName,  
                               "Bean with name '" + beanName + "' has been injected into other beans [" +  
                               StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +  
                               "] in its raw version as part of a circular reference, but has eventually been " +  
                               "wrapped. This means that said other beans do not use the final version of the " +  
                               "bean. This is often the result of over-eager type matching - consider using " +  
                               "'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");  
                   }  
               }  
           }  
       }  
       //注册完成依赖注入的Bean  
       try {  
           registerDisposableBeanIfNecessary(beanName, bean, mbd);  
       }  
       catch (BeanDefinitionValidationException ex) {  
           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);  
       }  
       return exposedObject;  
    }
```

# createBeanInstance


```
   protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args) {  
       //检查确认Bean是可实例化的  
       Class beanClass = resolveBeanClass(mbd, beanName);  
       //使用工厂方法对Bean进行实例化  
       if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {  
           throw new BeanCreationException(mbd.getResourceDescription(), beanName,  
                   "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());  
       }  
       if (mbd.getFactoryMethodName() != null)  {  
           //调用工厂方法实例化  
           return instantiateUsingFactoryMethod(beanName, mbd, args);  
       }  
       //使用容器的自动装配方法进行实例化  
       boolean resolved = false;  
       boolean autowireNecessary = false;  
       if (args == null) {  
           synchronized (mbd.constructorArgumentLock) {  
               if (mbd.resolvedConstructorOrFactoryMethod != null) {  
                   resolved = true;  
                   autowireNecessary = mbd.constructorArgumentsResolved;  
               }  
           }  
       }  
       if (resolved) {  
           if (autowireNecessary) {  
               //配置了自动装配属性，使用容器的自动装配实例化  
               //容器的自动装配是根据参数类型匹配Bean的构造方法  
               return autowireConstructor(beanName, mbd, null, null);  
           }  
           else {  
               //使用默认的无参构造方法实例化  
               return instantiateBean(beanName, mbd);  
           }  
       }  
       //使用Bean的构造方法进行实例化  
       Constructor[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);  
       if (ctors != null ||  
               mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||  
               mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {  
           //使用容器的自动装配特性，调用匹配的构造方法实例化  
           return autowireConstructor(beanName, mbd, ctors, args);  
       }  
       //使用默认的无参构造方法实例化  
       return instantiateBean(beanName, mbd);  
   }   

```

# instantiateBean


```
   //使用默认的无参构造方法实例化Bean对象  
   protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {  
       try {  
           Object beanInstance;  
           final BeanFactory parent = this;  
           //获取系统的安全管理接口，JDK标准的安全管理API  
           if (System.getSecurityManager() != null) {  
               //这里是一个匿名内置类，根据实例化策略创建实例对象  
               beanInstance = AccessController.doPrivileged(new PrivilegedAction<Object>() {  
                   public Object run() {  
                       return getInstantiationStrategy().instantiate(mbd, beanName, parent);  
                   }  
               }, getAccessControlContext());  
           }  
           else {  
               //将实例化的对象封装起来  
               beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);  
           }  
           BeanWrapper bw = new BeanWrapperImpl(beanInstance);  
           initBeanWrapper(bw);  
           return bw;  
       }  
       catch (Throwable ex) {  
           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);  
       }  
   }
```

# instantiate


```
//使用初始化策略实例化Bean对象  
   public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner) {  
       //如果Bean定义中没有方法覆盖，则就不需要CGLIB父类类的方法  
       if (beanDefinition.getMethodOverrides().isEmpty()) {  
           Constructor<?> constructorToUse;  
           synchronized (beanDefinition.constructorArgumentLock) {  
               //获取对象的构造方法或工厂方法  
               constructorToUse = (Constructor<?>) beanDefinition.resolvedConstructorOrFactoryMethod;  
               //如果没有构造方法且没有工厂方法  
               if (constructorToUse == null) {  
                   //使用JDK的反射机制，判断要实例化的Bean是否是接口  
                   final Class clazz = beanDefinition.getBeanClass();  
                   if (clazz.isInterface()) {  
                       throw new BeanInstantiationException(clazz, "Specified class is an interface");  
                   }  
                   try {  
                       if (System.getSecurityManager() != null) {  
                       //这里是一个匿名内置类，使用反射机制获取Bean的构造方法  
                           constructorToUse = AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor>() {  
                               public Constructor run() throws Exception {  
                                   return clazz.getDeclaredConstructor((Class[]) null);  
                               }  
                           });  
                       }  
                       else {  
                           constructorToUse =  clazz.getDeclaredConstructor((Class[]) null);  
                       }  
                       beanDefinition.resolvedConstructorOrFactoryMethod = constructorToUse;  
                   }  
                   catch (Exception ex) {  
                       throw new BeanInstantiationException(clazz, "No default constructor found", ex);  
                   }  
               }  
           }  
           //使用BeanUtils实例化，通过反射机制调用”构造方法.newInstance(arg)”来进行实例化  
           return BeanUtils.instantiateClass(constructorToUse);  
       }  
       else {  
           //使用CGLIB来实例化对象  
           return instantiateWithMethodInjection(beanDefinition, beanName, owner);  
       }  
    }
```


# instantiate


```
//使用CGLIB进行Bean对象实例化  
   public Object instantiate(Constructor ctor, Object[] args) {  
           //CGLIB中的类  
           Enhancer enhancer = new Enhancer();  
           //将Bean本身作为其基类  
           enhancer.setSuperclass(this.beanDefinition.getBeanClass());  
           enhancer.setCallbackFilter(new CallbackFilterImpl());  
           enhancer.setCallbacks(new Callback[] {  
                   NoOp.INSTANCE,  
                   new LookupOverrideMethodInterceptor(),  
                   new ReplaceOverrideMethodInterceptor()  
           });  
           //使用CGLIB的create方法生成实例对象  
           return (ctor == null) ?   
                   enhancer.create() :   
                   enhancer.create(ctor.getParameterTypes(), args);  
       }
```
# createBean


```

```


