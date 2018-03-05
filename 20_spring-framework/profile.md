
属性文件
application-dev.properties(环境文件)
application.properties(公共文件)
公共文件会覆盖环境文件
 
 
传入
tomcat
tomcat 中 catalina.bat（.sh中不用“set”） 添加JAVA_OPS。
set JAVA_OPTS="-Dspring.profiles.active=test"
 
eclipse内置tomcat
	项目右键 run as –> run configuration–>Arguments–> VM arguments
	-Dspring.profiles.active=dev
 
 
识别
	java
@Autowired private Environment env;
	String property = env.getProperty("spring.profiles.active");
 
http://www.cnblogs.com/pangguoming/p/5888871.html
 
 
 
