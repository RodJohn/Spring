


aop 和 自定义注解 

http://blog.csdn.net/fighterandknight/article/details/51170746





用@Autowired搭配@Qualifier("你需注入的其中一个实现类类名首字母小写或者自己取的bean id的名字")；另外还有个@Primary注解，它可以让Spring在注入bean时，当有多个实现类，Spring就只注入有@Primary注解的那个实现bean。




