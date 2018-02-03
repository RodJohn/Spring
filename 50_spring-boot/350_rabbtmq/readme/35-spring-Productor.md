
Productor
Message
Message Builder

Productor


    @Autowired
    private AmqpTemplate rabbitTemplate;

    this.rabbitTemplate.convertAndSend(exchangeName,routingKey, new Person("李俊",27));

    public void convertAndSend(String exchange, String routingKey, final Object object, CorrelationData correlationData) throws AmqpException {
		send(exchange, routingKey, convertMessageIfNecessary(object), correlationData);
	}

生产者
注入操作模板
@Autowired private AmqpTemplate rabbitTemplate;
发送方法
direct-最简模式
this.rabbitTemplate.convertAndSend(queueName,  "hello " + new Date())
其他模式
this.rabbitTemplate.convertAndSend(exchangeName,queueName,  "hello " + new Date());


void send(String exchange, String routingKey, Message message) throws AmqpException;
我们将使用上面列出的最后一个方法来讨论，因为它实际是最清晰的.它允许在运行时提供一个AMQP Exchange 名称和路由键(routing key).最后一个参数是负责初建创建Message实例的回调.使用此方法来发送消息的示例如下:



关于交换器和路由键更好的想法是明确的参数将总是会覆盖模板默认值.事实上, 即使你不在模板上明确设置这些属性, 总是有默认值的地方. 在两种情况中，默认值是空字符串，这是合情合理的． 
就路由键而言，它并不总是首先需要的 (如． Fanout 交换器). 此外,绑定的交换器上的队列可能会使用空字符串. 这些在模板的路由键中都是合法的. 
就交换器名称而言，空字符串也是常常使用的，因为AMQP规范定义了无名称的"默认交换器".
由于所有队列可使用它们的队列名称作为路由键自动绑定到默认交换器上(它是Direct交换器e) ,上面的第二个方法可通过默认的交换器将简单的点对点消息传递到任何队列. 
只需要简单的将队列名称作为路由键-在运行时提供方法参数：


Message 

public　class Message {

    private　final MessageProperties messageProperties;

    private　final　byte[] body;

    public Message(byte[] body, MessageProperties messageProperties) {
        this.body = body;
        this.messageProperties = messageProperties;
    }

    public　byte[] getBody() {
        returnthis.body;
    }

    public MessageProperties getMessageProperties() {
        returnthis.messageProperties;
    }
}

Message Builder

从1.3版本开始,通过 MessageBuilder 和 MessagePropertiesBuilder提供了消息构建API; 它们提供了更加方便地创建消息和消息属性的方法:
Message message = MessageBuilder.withBody("foo".getBytes())
	.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
	.setMessageId("123")
	.setHeader("bar", "baz")
	.build();
或
MessageProperties props = MessagePropertiesBuilder.newInstance()
	.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
	.setMessageId("123")
	.setHeader("bar", "baz")
	.build();
Message message = MessageBuilder.withBody("foo".getBytes())
	.andProperties(props)
	.build();


每个MessageProperies上定义的属性都可以被设置. 其它方法包括setHeader(String key, String value),removeHeader(String key), removeHeaders(), 和copyProperties(MessageProperties properties). 
每个属性方法都有一个set*IfAbsent() 变种. 在默认的初始值存在的情况下, 方法名为set*IfAbsentOrDefault().


默认

SimpleMessageConverter
对于使用Serializable进行jdk序列化

MessageConverter 策略的默认实现被称为SimpleMessageConverter. 如果你没有明确配置，RabbitTemplate实例会使用此转换器的实例.它能处理基于文本内容，序列化Java对象，以及简单的字节数组．

自定义

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
