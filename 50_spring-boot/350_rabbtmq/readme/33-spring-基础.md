
参考
ConnectionFactory
AmqpTemplate
依赖/连接

参考

https://spring.io/guides/gs/messaging-rabbitmq/



ConnectionFactory 

RabbitMQ broker中用于管理连接的中心组件是ConnectionFactory 接口. ConnectionFactory实现的责任是提供一个org.springframework.amqp.rabbit.connection.Connection 的实例，它包装了com.rabbitmq.client.Connection. 
我们提供的唯一具体实现提CachingConnectionFactory

CachingConnectionFactory connectionFactory = new CachingConnectionFactory("somehost");
connectionFactory.setUsername("guest");
connectionFactory.setPassword("guest");
Connection connection = connectionFactory.createConnection();

如果你想配置channel缓存的大小(默认是25),你可以调用setChannelCacheSize()方法.
从1.3版本开始,CachingConnectionFactory 也可以同channel一样，配置缓存连接.在这种情况下每次调用createConnection() 都会创建一个新连接(或者从缓存中获取空闲的连接).




AmqpTemplate


添加重试功能

从１.3版本开始， 你可为RabbitTemplate 配置使用 RetryTemplate 来帮助处理broker连接的问题. 参考spring-retry 项目来了解全部信息;下面就是一个例子，它使用指数回退策略(exponential back off policy)和默认的 SimpleRetryPolicy (向调用者抛出异常前，会做三次尝试）.

使用 @Configuration:
@Bean
public AmqpTemplate rabbitTemplate();
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(500);
		backOffPolicy.setMultiplier(10.0);
		backOffPolicy.setMaxInterval(10000);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		template.setRetryTemplate(retryTemplate);
		return template;
}




依赖/连接

依赖
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-amqp</artifactId>
</dependency>


使用配置文件生成连接
spring.rabbitmq.host=39.108.224.136
spring.rabbitmq.port=5672
spring.rabbitmq.virtual-host=test
spring.rabbitmq.username=admin
spring.rabbitmq.password=123456
spring.rabbitmq.listener.acknowledge-mode=auto   指定Acknowledge的模式默认.

spring.rabbitmq.addresses指定client连接到的server的地址，多个以逗号分隔.
spring.rabbitmq.dynamic是否创建AmqpAdmin bean. 默认为: true)
spring.rabbitmq.host指定RabbitMQ host.默认为: localhost)
spring.rabbitmq.listener.auto-startup是否在启动时就启动mq，默认: true)
spring.rabbitmq.listener.concurrency指定最小的消费者数量.
spring.rabbitmq.listener.max-concurrency指定最大的消费者数量.
spring.rabbitmq.listener.prefetch指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.
spring.rabbitmq.listener.transaction-size指定一个事务处理的消息数量，最好是小于等于prefetch的数量.
spring.rabbitmq.password指定broker的密码.
spring.rabbitmq.port指定RabbitMQ 的端口，默认: 5672)
spring.rabbitmq.requested-heartbeat指定心跳超时，0为不指定.
spring.rabbitmq.ssl.enabled是否开始SSL，默认: false)
spring.rabbitmq.ssl.key-store指定持有SSL certificate的key store的路径
spring.rabbitmq.ssl.key-store-password指定访问key store的密码.
spring.rabbitmq.ssl.trust-store指定持有SSL certificates的Trust store.
spring.rabbitmq.ssl.trust-store-password指定访问trust store的密码.
spring.rabbitmq.username指定登陆broker的用户名.
spring.rabbitmq.virtual-host指定连接到broker的Virtual host.






