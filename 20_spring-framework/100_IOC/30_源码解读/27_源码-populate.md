

# populateBean

```
   protected void populateBean(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw) {  
       //获取容器在解析Bean定义资源时为BeanDefiniton中设置的属性值  
       PropertyValues pvs = mbd.getPropertyValues();  
       //实例对象为null  
       if (bw == null) {  
           //属性值不为空  
           if (!pvs.isEmpty()) {  
               throw new BeanCreationException(  
                       mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");  
           }  
           else {  
               //实例对象为null，属性值也为空，不需要设置属性值，直接返回  
               return;  
           }  
       }  
       //在设置属性之前调用Bean的PostProcessor后置处理器  
       boolean continueWithPropertyPopulation = true;  
       if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {  
           for (BeanPostProcessor bp : getBeanPostProcessors()) {  
               if (bp instanceof InstantiationAwareBeanPostProcessor) {  
                   InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;  
                   if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {  
                       continueWithPropertyPopulation = false;  
                       break;  
                   }  
               }  
           }  
       }  
       if (!continueWithPropertyPopulation) {  
           return;  
       }  
       //依赖注入开始，首先处理autowire自动装配的注入  
       if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME ||  
               mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {  
           MutablePropertyValues newPvs = new MutablePropertyValues(pvs);  
           //对autowire自动装配的处理，根据Bean名称自动装配注入  
           if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME) {  
               autowireByName(beanName, mbd, bw, newPvs);  
           }  
           //根据Bean类型自动装配注入  
           if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {  
               autowireByType(beanName, mbd, bw, newPvs);  
           }  
           pvs = newPvs;  
       }  
       //检查容器是否持有用于处理单态模式Bean关闭时的后置处理器  
       boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();  
       //Bean实例对象没有依赖，即没有继承基类  
       boolean needsDepCheck = (mbd.getDependencyCheck() != RootBeanDefinition.DEPENDENCY_CHECK_NONE);  
       if (hasInstAwareBpps || needsDepCheck) {  
           //从实例对象中提取属性描述符  
           PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw);  
           if (hasInstAwareBpps) {  
               for (BeanPostProcessor bp : getBeanPostProcessors()) {  
                   if (bp instanceof InstantiationAwareBeanPostProcessor) {  
                       InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;  
                       //使用BeanPostProcessor处理器处理属性值  
                       pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);  
                       if (pvs == null) {  
                           return;  
                       }  
                   }  
               }  
           }  
           if (needsDepCheck) {  
               //为要设置的属性进行依赖检查  
               checkDependencies(beanName, mbd, filteredPds, pvs);  
           }  
       }  
       //对属性进行注入  
       applyPropertyValues(beanName, mbd, bw, pvs);  
   }  
```


# applyPropertyValues

```
 //解析并注入依赖属性的过程  
   protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs) {  
       if (pvs == null || pvs.isEmpty()) {  
           return;  
       }  
       //封装属性值  
       MutablePropertyValues mpvs = null;  
       List<PropertyValue> original;  
       if (System.getSecurityManager()!= null) {  
           if (bw instanceof BeanWrapperImpl) {  
               //设置安全上下文，JDK安全机制  
               ((BeanWrapperImpl) bw).setSecurityContext(getAccessControlContext());  
           }  
       }  
       if (pvs instanceof MutablePropertyValues) {  
           mpvs = (MutablePropertyValues) pvs;  
           //属性值已经转换  
           if (mpvs.isConverted()) {  
               try {  
                   //为实例化对象设置属性值  
                   bw.setPropertyValues(mpvs);  
                   return;  
               }  
               catch (BeansException ex) {  
                   throw new BeanCreationException(  
                           mbd.getResourceDescription(), beanName, "Error setting property values", ex);  
               }  
           }  
           //获取属性值对象的原始类型值  
           original = mpvs.getPropertyValueList();  
       }  
       else {  
           original = Arrays.asList(pvs.getPropertyValues());  
       }  
       //获取用户自定义的类型转换  
       TypeConverter converter = getCustomTypeConverter();  
       if (converter == null) {  
           converter = bw;  
       }  
       //创建一个Bean定义属性值解析器，将Bean定义中的属性值解析为Bean实例对象  
       //的实际值  
       BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, converter);  
       //为属性的解析值创建一个拷贝，将拷贝的数据注入到实例对象中  
       List<PropertyValue> deepCopy = new ArrayList<PropertyValue>(original.size());  
       boolean resolveNecessary = false;  
       for (PropertyValue pv : original) {  
           //属性值不需要转换  
           if (pv.isConverted()) {  
               deepCopy.add(pv);  
           }  
           //属性值需要转换  
           else {  
               String propertyName = pv.getName();  
               //原始的属性值，即转换之前的属性值  
               Object originalValue = pv.getValue();  
               //转换属性值，例如将引用转换为IoC容器中实例化对象引用  
               Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);  
               //转换之后的属性值  
               Object convertedValue = resolvedValue;  
               //属性值是否可以转换  
               boolean convertible = bw.isWritableProperty(propertyName) &&  
                       !PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName);  
               if (convertible) {  
                   //使用用户自定义的类型转换器转换属性值  
                   convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);  
               }  
               //存储转换后的属性值，避免每次属性注入时的转换工作  
               if (resolvedValue == originalValue) {  
                   if (convertible) {  
                       //设置属性转换之后的值  
                       pv.setConvertedValue(convertedValue);  
                   }  
                   deepCopy.add(pv);  
               }  
               //属性是可转换的，且属性原始值是字符串类型，且属性的原始类型值不是  
               //动态生成的字符串，且属性的原始值不是集合或者数组类型  
               else if (convertible && originalValue instanceof TypedStringValue &&  
                       !((TypedStringValue) originalValue).isDynamic() &&  
                       !(convertedValue instanceof Collection || ObjectUtils.isArray(convertedValue))) {  
                   pv.setConvertedValue(convertedValue);  
                   deepCopy.add(pv);  
               }  
               else {  
                   resolveNecessary = true;  
                   //重新封装属性的值  
                   deepCopy.add(new PropertyValue(pv, convertedValue));  
               }  
           }  
       }  
       if (mpvs != null && !resolveNecessary) {  
           //标记属性值已经转换过  
           mpvs.setConverted();  
       }  
       //进行属性依赖注入  
       try {  
           bw.setPropertyValues(new MutablePropertyValues(deepCopy));  
       }  
       catch (BeansException ex) {  
           throw new BeanCreationException(  
                   mbd.getResourceDescription(), beanName, "Error setting property values", ex);  
       }  
    }
```


(1).属性值类型不需要转换时，不需要解析属性值，直接准备进行依赖注入。

(2).属性值需要进行类型转换时，如对其他对象的引用等，首先需要解析属性值，然后对解析后的属性值进行依赖注入。

当容器在对属性进行依赖注入时，如果发现属性值需要进行类型转换，如属性值是容器中另一个Bean实例对象的引用，
则容器首先需要根据属性值解析出所引用的对象，然后才能将该引用对象注入到目标实例对象的属性上去，

# resolveValueIfNecessary



```
   public Object resolveValueIfNecessary(Object argName, Object value) {  
       //对引用类型的属性进行解析  
       if (value instanceof RuntimeBeanReference) {  
           RuntimeBeanReference ref = (RuntimeBeanReference) value;  
           //调用引用类型属性的解析方法  
           return resolveReference(argName, ref);  
       }  
       //对属性值是引用容器中另一个Bean名称的解析  
       else if (value instanceof RuntimeBeanNameReference) {  
           String refName = ((RuntimeBeanNameReference) value).getBeanName();  
           refName = String.valueOf(evaluate(refName));  
           //从容器中获取指定名称的Bean  
           if (!this.beanFactory.containsBean(refName)) {  
               throw new BeanDefinitionStoreException(  
                       "Invalid bean name '" + refName + "' in bean reference for " + argName);  
           }  
           return refName;  
       }  
       //对Bean类型属性的解析，主要是Bean中的内部类  
       else if (value instanceof BeanDefinitionHolder) {  
           BeanDefinitionHolder bdHolder = (BeanDefinitionHolder) value;  
           return resolveInnerBean(argName, bdHolder.getBeanName(), bdHolder.getBeanDefinition());  
       }  
       else if (value instanceof BeanDefinition) {  
           BeanDefinition bd = (BeanDefinition) value;  
           return resolveInnerBean(argName, "(inner bean)", bd);  
       }  
       //对集合数组类型的属性解析  
       else if (value instanceof ManagedArray) {  
           ManagedArray array = (ManagedArray) value;  
           //获取数组的类型  
           Class elementType = array.resolvedElementType;  
           if (elementType == null) {  
               //获取数组元素的类型  
               String elementTypeName = array.getElementTypeName();  
               if (StringUtils.hasText(elementTypeName)) {  
                   try {  
                       //使用反射机制创建指定类型的对象  
                       elementType = ClassUtils.forName(elementTypeName, this.beanFactory.getBeanClassLoader());  
                       array.resolvedElementType = elementType;  
                   }  
                   catch (Throwable ex) {  
                       throw new BeanCreationException(  
                               this.beanDefinition.getResourceDescription(), this.beanName,  
                               "Error resolving array type for " + argName, ex);  
                   }  
               }  
               //没有获取到数组的类型，也没有获取到数组元素的类型，则直接设置数  
               //组的类型为Object  
               else {  
                   elementType = Object.class;  
               }  
           }  
           //创建指定类型的数组  
           return resolveManagedArray(argName, (List<?>) value, elementType);  
       }  
       //解析list类型的属性值  
       else if (value instanceof ManagedList) {  
           return resolveManagedList(argName, (List<?>) value);  
       }  
       //解析set类型的属性值  
       else if (value instanceof ManagedSet) {  
           return resolveManagedSet(argName, (Set<?>) value);  
       }  
       //解析map类型的属性值  
       else if (value instanceof ManagedMap) {  
           return resolveManagedMap(argName, (Map<?, ?>) value);  
       }  
       //解析props类型的属性值，props其实就是key和value均为字符串的map  
       else if (value instanceof ManagedProperties) {  
           Properties original = (Properties) value;  
           //创建一个拷贝，用于作为解析后的返回值  
           Properties copy = new Properties();  
           for (Map.Entry propEntry : original.entrySet()) {  
               Object propKey = propEntry.getKey();  
               Object propValue = propEntry.getValue();  
               if (propKey instanceof TypedStringValue) {  
                   propKey = evaluate((TypedStringValue) propKey);  
               }  
               if (propValue instanceof TypedStringValue) {  
                   propValue = evaluate((TypedStringValue) propValue);  
               }  
               copy.put(propKey, propValue);  
           }  
           return copy;  
       }  
       //解析字符串类型的属性值  
       else if (value instanceof TypedStringValue) {  
           TypedStringValue typedStringValue = (TypedStringValue) value;  
           Object valueObject = evaluate(typedStringValue);  
           try {  
               //获取属性的目标类型  
               Class<?> resolvedTargetType = resolveTargetType(typedStringValue);  
               if (resolvedTargetType != null) {  
                   //对目标类型的属性进行解析，递归调用  
                   return this.typeConverter.convertIfNecessary(valueObject, resolvedTargetType);  
               }  
               //没有获取到属性的目标对象，则按Object类型返回  
               else {  
                   return valueObject;  
               }  
           }  
           catch (Throwable ex) {  
               throw new BeanCreationException(  
                       this.beanDefinition.getResourceDescription(), this.beanName,  
                       "Error converting typed String value for " + argName, ex);  
           }  
       }  
       else {  
           return evaluate(value);  
       }  
   }  
```


## resolveReference

```
   //解析引用类型的属性值  
   private Object resolveReference(Object argName, RuntimeBeanReference ref) {  
       try {  
           //获取引用的Bean名称  
           String refName = ref.getBeanName();  
           refName = String.valueOf(evaluate(refName));  
           //如果引用的对象在父类容器中，则从父类容器中获取指定的引用对象  
           if (ref.isToParent()) {  
               if (this.beanFactory.getParentBeanFactory() == null) {  
                   throw new BeanCreationException(  
                           this.beanDefinition.getResourceDescription(), this.beanName,  
                           "Can't resolve reference to bean '" + refName +  
                           "' in parent factory: no parent factory available");  
               }  
               return this.beanFactory.getParentBeanFactory().getBean(refName);  
           }  
           //从当前的容器中获取指定的引用Bean对象，如果指定的Bean没有被实例化  
           //则会递归触发引用Bean的初始化和依赖注入  
           else {  
               Object bean = this.beanFactory.getBean(refName);  
               //将当前实例化对象的依赖引用对象  
               this.beanFactory.registerDependentBean(refName, this.beanName);  
               return bean;  
           }  
       }  
       catch (BeansException ex) {  
           throw new BeanCreationException(  
                   this.beanDefinition.getResourceDescription(), this.beanName,  
                   "Cannot resolve reference to bean '" + ref.getBeanName() + "' while setting " + argName, ex);  
       }  
   }   
```

## 

```
   //解析array类型的属性  
   private Object resolveManagedArray(Object argName, List<?> ml, Class elementType) {  
       //创建一个指定类型的数组，用于存放和返回解析后的数组  
       Object resolved = Array.newInstance(elementType, ml.size());  
       for (int i = 0; i < ml.size(); i++) {  
       //递归解析array的每一个元素，并将解析后的值设置到resolved数组中，索引为i  
           Array.set(resolved, i,  
               resolveValueIfNecessary(new KeyedArgName(argName, i), ml.get(i)));  
       }  
       return resolved;  
   }  
   //解析list类型的属性  
   private List resolveManagedList(Object argName, List<?> ml) {  
       List<Object> resolved = new ArrayList<Object>(ml.size());  
       for (int i = 0; i < ml.size(); i++) {  
           //递归解析list的每一个元素  
           resolved.add(  
               resolveValueIfNecessary(new KeyedArgName(argName, i), ml.get(i)));  
       }  
       return resolved;  
   }  
   //解析set类型的属性  
   private Set resolveManagedSet(Object argName, Set<?> ms) {  
       Set<Object> resolved = new LinkedHashSet<Object>(ms.size());  
       int i = 0;  
       //递归解析set的每一个元素  
       for (Object m : ms) {  
           resolved.add(resolveValueIfNecessary(new KeyedArgName(argName, i), m));  
           i++;  
       }  
       return resolved;  
   }  
   //解析map类型的属性  
   private Map resolveManagedMap(Object argName, Map<?, ?> mm) {  
       Map<Object, Object> resolved = new LinkedHashMap<Object, Object>(mm.size());  
       //递归解析map中每一个元素的key和value  
       for (Map.Entry entry : mm.entrySet()) {  
           Object resolvedKey = resolveValueIfNecessary(argName, entry.getKey());  
           Object resolvedValue = resolveValueIfNecessary(  
                   new KeyedArgName(argName, entry.getKey()), entry.getValue());  
           resolved.put(resolvedKey, resolvedValue);  
       }  
       return resolved;  
   }
```
# 

```
   private void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException {  
       //PropertyTokenHolder主要保存属性的名称、路径，以及集合的size等信息  
       String propertyName = tokens.canonicalName;  
       String actualName = tokens.actualName;  
       //keys是用来保存集合类型属性的size  
       if (tokens.keys != null) {  
           //将属性信息拷贝  
           PropertyTokenHolder getterTokens = new PropertyTokenHolder();  
           getterTokens.canonicalName = tokens.canonicalName;  
           getterTokens.actualName = tokens.actualName;  
           getterTokens.keys = new String[tokens.keys.length - 1];  
           System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);  
           Object propValue;  
           try {  
               //获取属性值，该方法内部使用JDK的内省( Introspector)机制，调用属性//的getter(readerMethod)方法，获取属性的值  
               propValue = getPropertyValue(getterTokens);  
           }  
           catch (NotReadablePropertyException ex) {  
               throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName,  
                       "Cannot access indexed value in property referenced " +  
                       "in indexed property path '" + propertyName + "'", ex);  
           }  
           //获取集合类型属性的长度  
           String key = tokens.keys[tokens.keys.length - 1];  
           if (propValue == null) {  
               throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName,  
                       "Cannot access indexed value in property referenced " +  
                       "in indexed property path '" + propertyName + "': returned null");  
           }  
           //注入array类型的属性值  
           else if (propValue.getClass().isArray()) {  
               //获取属性的描述符  
               PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);  
               //获取数组的类型  
               Class requiredType = propValue.getClass().getComponentType();  
               //获取数组的长度  
               int arrayIndex = Integer.parseInt(key);  
               Object oldValue = null;  
               try {  
                   //获取数组以前初始化的值  
                   if (isExtractOldValueForEditor()) {  
                       oldValue = Array.get(propValue, arrayIndex);  
                   }  
                   //将属性的值赋值给数组中的元素  
                   Object convertedValue = convertIfNecessary(propertyName, oldValue, pv.getValue(), requiredType,  
                           new PropertyTypeDescriptor(pd, new MethodParameter(pd.getReadMethod(), -1), requiredType));  
                   Array.set(propValue, arrayIndex, convertedValue);  
               }  
               catch (IndexOutOfBoundsException ex) {  
                   throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,  
                           "Invalid array index in property path '" + propertyName + "'", ex);  
               }  
           }  
           //注入list类型的属性值  
           else if (propValue instanceof List) {  
               PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);  
               //获取list集合的类型  
               Class requiredType = GenericCollectionTypeResolver.getCollectionReturnType(  
                       pd.getReadMethod(), tokens.keys.length);  
               List list = (List) propValue;  
               //获取list集合的size  
               int index = Integer.parseInt(key);  
               Object oldValue = null;  
               if (isExtractOldValueForEditor() && index < list.size()) {  
                   oldValue = list.get(index);  
               }  
               //获取list解析后的属性值  
               Object convertedValue = convertIfNecessary(propertyName, oldValue, pv.getValue(), requiredType,  
                       new PropertyTypeDescriptor(pd, new MethodParameter(pd.getReadMethod(), -1), requiredType));  
               if (index < list.size()) {  
                   //为list属性赋值  
                   list.set(index, convertedValue);  
               }  
               //如果list的长度大于属性值的长度，则多余的元素赋值为null  
               else if (index >= list.size()) {  
                   for (int i = list.size(); i < index; i++) {  
                       try {  
                           list.add(null);  
                       }  
                       catch (NullPointerException ex) {  
                           throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,  
                                   "Cannot set element with index " + index + " in List of size " +  
                                   list.size() + ", accessed using property path '" + propertyName +  
                                   "': List does not support filling up gaps with null elements");  
                       }  
                   }  
                   list.add(convertedValue);  
               }  
           }  
           //注入map类型的属性值  
           else if (propValue instanceof Map) {  
               PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);  
               //获取map集合key的类型  
               Class mapKeyType = GenericCollectionTypeResolver.getMapKeyReturnType(  
                       pd.getReadMethod(), tokens.keys.length);  
               //获取map集合value的类型  
               Class mapValueType = GenericCollectionTypeResolver.getMapValueReturnType(  
                       pd.getReadMethod(), tokens.keys.length);  
               Map map = (Map) propValue;  
               //解析map类型属性key值  
               Object convertedMapKey = convertIfNecessary(null, null, key, mapKeyType,  
                       new PropertyTypeDescriptor(pd, new MethodParameter(pd.getReadMethod(), -1), mapKeyType));  
               Object oldValue = null;  
               if (isExtractOldValueForEditor()) {  
                   oldValue = map.get(convertedMapKey);  
               }  
               //解析map类型属性value值  
               Object convertedMapValue = convertIfNecessary(  
                       propertyName, oldValue, pv.getValue(), mapValueType,  
                       new TypeDescriptor(new MethodParameter(pd.getReadMethod(), -1, tokens.keys.length + 1)));  
               //将解析后的key和value值赋值给map集合属性  
               map.put(convertedMapKey, convertedMapValue);  
           }  
           else {  
               throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName,  
                       "Property referenced in indexed property path '" + propertyName +  
                       "' is neither an array nor a List nor a Map; returned value was [" + pv.getValue() + "]");  
           }  
       }  
       //对非集合类型的属性注入  
       else {  
           PropertyDescriptor pd = pv.resolvedDescriptor;  
           if (pd == null || !pd.getWriteMethod().getDeclaringClass().isInstance(this.object)) {  
               pd = getCachedIntrospectionResults().getPropertyDescriptor(actualName);  
               //无法获取到属性名或者属性没有提供setter(写方法)方法  
               if (pd == null || pd.getWriteMethod() == null) {  
                   //如果属性值是可选的，即不是必须的，则忽略该属性值  
                   if (pv.isOptional()) {  
                       logger.debug("Ignoring optional value for property '" + actualName +  
                               "' - property not found on bean class [" + getRootClass().getName() + "]");  
                       return;  
                   }  
                   //如果属性值是必须的，则抛出无法给属性赋值，因为每天提供setter方法异常  
                   else {  
                       PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());  
                       throw new NotWritablePropertyException(  
                               getRootClass(), this.nestedPath + propertyName,  
                               matches.buildErrorMessage(), matches.getPossibleMatches());  
                   }  
               }  
               pv.getOriginalPropertyValue().resolvedDescriptor = pd;  
           }  
           Object oldValue = null;  
           try {  
               Object originalValue = pv.getValue();  
               Object valueToApply = originalValue;  
               if (!Boolean.FALSE.equals(pv.conversionNecessary)) {  
                   if (pv.isConverted()) {  
                       valueToApply = pv.getConvertedValue();  
                   }  
                   else {  
                       if (isExtractOldValueForEditor() && pd.getReadMethod() != null) {  
                           //获取属性的getter方法(读方法)，JDK内省机制  
                           final Method readMethod = pd.getReadMethod();  
                           //如果属性的getter方法不是public访问控制权限的，即访问控制权限比较严格，  
                           //则使用JDK的反射机制强行访问非public的方法(暴力读取属性值)  
                           if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()) &&  
                                   !readMethod.isAccessible()) {  
                               if (System.getSecurityManager()!= null) {  
                                   //匿名内部类，根据权限修改属性的读取控制限制  
                                   AccessController.doPrivileged(new PrivilegedAction<Object>() {  
                                       public Object run() {  
                                           readMethod.setAccessible(true);  
                                           return null;  
                                       }  
                                   });  
                               }  
                               else {  
                                   readMethod.setAccessible(true);  
                               }  
                           }  
                           try {  
                               //属性没有提供getter方法时，调用潜在的读取属性值//的方法，获取属性值  
                               if (System.getSecurityManager() != null) {  
                                   oldValue = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {  
                                       public Object run() throws Exception {  
                                           return readMethod.invoke(object);  
                                       }  
                                   }, acc);  
                               }  
                               else {  
                                   oldValue = readMethod.invoke(object);  
                               }  
                           }  
                           catch (Exception ex) {  
                               if (ex instanceof PrivilegedActionException) {  
                                   ex = ((PrivilegedActionException) ex).getException();  
                               }  
                               if (logger.isDebugEnabled()) {  
                                   logger.debug("Could not read previous value of property '" +  
                                           this.nestedPath + propertyName + "'", ex);  
                               }  
                           }  
                       }  
                       //设置属性的注入值  
                       valueToApply = convertForProperty(propertyName, oldValue, originalValue, pd);  
                   }  
                   pv.getOriginalPropertyValue().conversionNecessary = (valueToApply != originalValue);  
               }  
               //根据JDK的内省机制，获取属性的setter(写方法)方法  
               final Method writeMethod = (pd instanceof GenericTypeAwarePropertyDescriptor ?  
                       ((GenericTypeAwarePropertyDescriptor) pd).getWriteMethodForActualAccess() :  
                       pd.getWriteMethod());  
               //如果属性的setter方法是非public，即访问控制权限比较严格，则使用JDK的反射机制，  
               //强行设置setter方法可访问(暴力为属性赋值)  
               if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers()) && !writeMethod.isAccessible()) {  
                   //如果使用了JDK的安全机制，则需要权限验证  
                   if (System.getSecurityManager()!= null) {  
                       AccessController.doPrivileged(new PrivilegedAction<Object>() {  
                           public Object run() {  
                               writeMethod.setAccessible(true);  
                               return null;  
                           }  
                       });  
                   }  
                   else {  
                       writeMethod.setAccessible(true);  
                   }  
               }  
               final Object value = valueToApply;  
               if (System.getSecurityManager() != null) {  
                   try {  
                       //将属性值设置到属性上去  
                       AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {  
                           public Object run() throws Exception {  
                               writeMethod.invoke(object, value);  
                               return null;  
                           }  
                       }, acc);  
                   }  
                   catch (PrivilegedActionException ex) {  
                       throw ex.getException();  
                   }  
               }  
               else {  
                   writeMethod.invoke(this.object, value);  
               }  
           }  
           catch (TypeMismatchException ex) {  
               throw ex;  
           }  
           catch (InvocationTargetException ex) {  
               PropertyChangeEvent propertyChangeEvent =  
                       new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());  
               if (ex.getTargetException() instanceof ClassCastException) {  
                   throw new TypeMismatchException(propertyChangeEvent, pd.getPropertyType(), ex.getTargetException());  
               }  
               else {  
                   throw new MethodInvocationException(propertyChangeEvent, ex.getTargetException());  
               }  
           }  
           catch (Exception ex) {  
               PropertyChangeEvent pce =  
                       new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, pv.getValue());  
               throw new MethodInvocationException(pce, ex);  
           }  
       }  
    }
```

(1).对于集合类型的属性，将其属性值解析为目标类型的集合后直接赋值给属性。

(2).对于非集合类型的属性，大量使用了JDK的反射和内省机制，通过属性的getter方法(reader method)获取指定属性注入以前的值，
同时调用属性的setter方法(writer method)为属性设置注入后的值。看到这里相信很多人都明白了Spring的setter注入原理。

# 

```

```

