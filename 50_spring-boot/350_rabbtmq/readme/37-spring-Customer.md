
Customer
Rolling
MessageListenerContainer
注解
endpoint
Endpoint Method Signature
RabbitHandler
回复

Customer


Rolling


MessageListenerContainer

@Bean
SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {

    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
}
@Bean
Receiver receiver() {
    return new Receiver();
}

@Bean
MessageListenerAdapter listenerAdapter(Receiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
}

public class Receiver {
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }
}


注解

从1.4版本开始，异步接收消息的最简单方式是使用注解监听器端点
为了启用 @RabbitListener 注解，需要在你的某个@Configuration类中添加@EnableRabbit 注解．

endpoint

推荐使用最简单的配置,1.声明配置的功能不够完整. 2.配置和功能分开

简单配置
@Component
public　class MyService {      
　@RabbitListener(queues = "myQueue")
　public　void processOrder(String data) {         
　    ...     
 　}  
}
当消息在org.springframework.amqp.core.Queue "myQueue"上可用时, 会调用processOrder方法(在这种情况下，带有消息的负载).
要求myQueue 必须是事先存在的，并绑定了某个交换器上.

声明式配置
从1.5.0版本开始,只要在上下文中存在RabbitAdmin，队列可自动声明和绑定．

@Component
public　class MyService {

  @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "myQueue", durable = "true"),
        exchange = @Exchange(value = "auto.exch", ignoreDeclarationExceptions = "true"),
        key = "orderRoutingKey")
  )public　void processOrder(String data) {
    ...
  }

从１.６版本开始,你可为队列，交换器和绑定的@QueueBinding 注解中指定参数.示例:

@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "auto.headers", autoDelete = "true",
                        arguments = @Argument(name = "x-message-ttl", value = "10000",
                                                type = "java.lang.Integer")),
        exchange = @Exchange(value = "auto.headers", type = ExchangeTypes.HEADERS, autoDelete = "true"),
        arguments = {
                @Argument(name = "x-match", value = "all"),
                @Argument(name = "foo", value = "bar"),
                @Argument(name = "baz")
        })
)
public String handleWithHeadersExchange(String foo) {
    ...
}

注意队列的x-message-ttl 参数设为了10秒钟，因为参数类型不是String, 因此我们指定了它的类型，在这里是Integer.有了这些声明后，如果队列已经存在了，参数必须匹配现有队列上的参数.对于header交换器,我们设置binding arguments 要匹配头中foo为bar，且baz可为任意值的消息. x-match 参数则意味着必须同时满足两个条件.
参数名称，参数值，及类型可以是属性占位符(${...}) 或SpEL 表达式(#{...}). name 必须要能解析为String; type的表达式必须能解析为Class 或类的全限定名. value 必须能由DefaultConversionService 类型进行转换(如上面例子中x-message-ttl).
如果name 解析为null 或空字符串,那么将忽略 @Argumen


Endpoint Method Signature


下面是你可以在监听端点上注入的主要元素：
原生org.springframework.amqp.core.Message.
用于接收消息的com.rabbitmq.client.Channel
 org.springframework.messaging.Message 代表的是传入的AMQP消息.注意，这个消息持有自定义和标准的头部信息 (AmqpHeaders定义).

@Header-注解方法参数可 提取一个特定头部值，包括标准的AMQP头.
@Headers-注解参数为了访问所有头信息，必须能指定为java.util.Map.

非注解元素(非支持类型(如. Message 和Channel)）可认为是负荷(payload).你可以使用 @Payload来明确标识. 你也可以添加额外的 @Valid来进行验证.


    @RabbitListener(queues=queueName)
    public void process(
    		@Payload Person person,
//    		@Header("type")String type,
    		@Header(AmqpHeaders.CONSUMER_QUEUE)String queueName,
    		Message msg ,Channel cal) {
    	
        System.err.println("Receiver  : " + person);
    }

RabbitHandler

从1.5.0版本开始，@RabbitListener 注解现在可以在类级上进行指定.
与新的@RabbitHandler 注解一起，基于传入消息的负荷类型，这可以允许在单个监听器上调用不同的方法.
在这种情况下，独立的 @RabbitHandler 方法会被调用，如果转换后负荷是Bar, Baz 或Qux. 理解基于负荷类型系统来确定唯一方法是很重要的．类型检查是通过单个无注解参数来执行的，否则就要使用@Payload 进行注解. 注意同样的方法签名可应用于方法级 @RabbitListener 之上.注意，如果有必要，需要在每个方法上指定@SendTo， 在类级上它是不支持的．


@RabbitListener(id="multi", queues = "someQueue")
publicclass MultiListenerBean {

@RabbitHandler
public String bar(Bar bar) {
        ...
        }

       @RabbitHandler
       public String baz(Baz baz) {
        ...
       }

      @RabbitHandler
       public String qux(@Header("amqp_receivedRoutingKey") String rk, @Payload Qux qux) {
         ...
      }
}



回复

MessageListenerAdapter 现有的支持已经允许你的方法有一个非void的返回类型.在这种情况下，调用的结果被封装在一个发送消息中，其消息发送地址要么是原始消息的ReplyToAddress头指定的地址要么是监听器上配置的默认地址．默认地址现在可通过@SendTo 注解进行设置.

@RabbitListener(destination = "myQueue")
@SendTo("status")
public OrderStatus processOrder(Order order) {
    // order processing
　return status;
}

@SendTo 值按照exchange/routingKey模式(其中的一部分可以省略)来作为对exchange 和 routingKey 的回复．有效值为:
foo/bar - 以交换器和路由键进行回复.
foo/ - 以交换器和默认路由键进行回复.
bar or /bar - 以路由键和默认交换器进行回复.
/ or empty - 以默认交换器和默认路由键进行回复.
 @SendTo 也可以没有value 属性. 这种情况等价于空的sendTo 模式. @SendTo 只能应用于没有replyToAddress 属性的入站消息中.

从1.6版本开始， @SendTo 可以是SpEL 表达式，它可在运行时根据请求和回复来评估：
@RabbitListener(queues = "test.sendTo.spel")
@SendTo("!{'some.reply.queue.with.' + result.queueName}")
public Bar capitalizeWithSendToSpel(Foo foo) {
    return processTheFooAndReturnABar(foo);
}







