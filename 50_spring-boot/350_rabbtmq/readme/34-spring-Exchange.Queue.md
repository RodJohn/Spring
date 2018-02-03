
声明-队列/交换机/绑定
4.不同Exchange模式的例子
Direct/普通/最简
Topic
Headers
fanout
附件
参考



声明-队列/交换机/绑定

 注意
    	声明的队列和交换机,
如果rabbit服务器中不存在,则会自动创建
如果rabbit服务器中存在,则声明参数必须和服务器中一致,否则报错

Queue

    @Bean
    public Queue testQueue() {
        return new Queue(queueName);
    }

	public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
		Assert.notNull(name, "'name' cannot be null");
		this.name = name;
		this.durable = durable;
		this.exclusive = exclusive;
		this.autoDelete = autoDelete;
		this.arguments = arguments;
	}



Exchange


    @Bean
    public DirectExchange testExchange() {
        return new DirectExchange(exchangeName);
    }

	public AbstractExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
		super();
		this.name = name;
		this.durable = durable;
		this.autoDelete = autoDelete;
		if (arguments != null) {
			this.arguments = arguments;
		}
		else {
			this.arguments = new HashMap<String, Object>();
		}
	}

Binding 

    @Bean
    public Binding binding() {
    	return BindingBuilder.bind(testQueue()).to(testExchange()).with(routingKey);
    }



4.不同Exchange模式的例子

Direct/普通/最简

向指定交换机发送指定routekey的消息
交换机向绑定key等于routekey的队列发送消息

普通模式


设置消费者
@Component
public class HelloReceiver {
	@RabbitListener(queues = "1-hello-queue")
    	public void process(String hello) {
        	System.out.println("Receiver  : " + hello);
    	}
}

      设置队列/交换机/和绑定
@Component
public class HelloSender {

    @Bean
    public Queue testQueue() {
        return new Queue(queueName); 
    }

    @Bean
    public DirectExchange testExchange() {
        return new DirectExchange(exchangeName);
    }
    
    @Bean
    public Binding binding() {
    	return BindingBuilder.bind(testQueue()).to(testExchange()).with(routingKey);
    }
}


发送消息
@Autowired private AmqpTemplate rabbitTemplate;
@Override
	public void run(String... args) throws Exception {
		this.rabbitTemplate.convertAndSend(exchangeName,routingKey,  "hello " + new Date());
	}


最简模式

原理
Default Exchange这种是特殊的Direct Exchange，是rabbitmq内部默认的一个交换机。
该交换机的name是空字符串
所有queue都默认binding 到该交换机上。
所有binding到该交换机上的queue，routing-key都和queue的name一样。

消费者和连接定义形式同上

设置队列和交换机和绑定
    只需要定义一个

    @Bean
    public Queue testQueue() {
        return new Queue(queueName);
    }

发送消息
直接routekey就是队列的名字

@Autowired private AmqpTemplate rabbitTemplate;
@Override
	public void run(String... args) throws Exception {
		this.rabbitTemplate.convertAndSend(queueName,  "hello " + new Date());
	}
	
   
Topic
简介
生产者向指定交换机发送指定routekey的消息
交换机通过绑定的通配关系,将消息发给消费者
绑定通配
符号 .  为最小词的分隔
符号 # 匹配一个或多个词
符号 *  匹配仅仅一个
例如
usa.#    匹配 usa.news  usa.weather.1
“usa.*   匹配  usa.XXX 


设置队列和交换机

    @Bean
    public Queue testQueue() {
    	return new Queue(queueName);
    }
    @Bean
    public Queue testQueue1() {
    	return new Queue(queueName1);
    }
    
    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange(exchangeName);
    }
    

设置绑定   
    @Bean
    public Binding binding() {
    	return BindingBuilder.bind(testQueue()).to(testExchange()).with("*.key");
    }
    @Bean
    public Binding binding1() {
    	return BindingBuilder.bind(testQueue1()).to(testExchange()).with("key.#");
    }


设置消费者
    @RabbitListener(queues={queueName})
    public void process(@Payload String hello) {
        System.err.println("Receiver - *.key  : " + hello);
    }
    
    @RabbitListener(queues={queueName1})
    public void process1(@Payload String hello) {
    	System.err.println("Receiver - key.#  : " + hello);
    }


发送消息
	@Override
	public void run(String... args) throws Exception {
		this.rabbitTemplate.convertAndSend(exchangeName,"key.key","key.key");
		this.rabbitTemplate.convertAndSend(exchangeName,"key.1","key.1");
		this.rabbitTemplate.convertAndSend(exchangeName,"key.1.1","key.1.1");
		this.rabbitTemplate.convertAndSend(exchangeName,"1.key","1.key");
		this.rabbitTemplate.convertAndSend(exchangeName,"1.1.key","1.1.key");
	}

效果
	Receiver - key.#  : key.key
	Receiver - *.key  : key.key
	Receiver - key.#  : key.1
	Receiver - *.key  : 1.key
	Receiver - key.#  : key.1.1

Headers




fanout
简介
fanout路由又称为广播路由，
当使用fanout交换器时，他会将消息广播到与该交换器绑定的所有队列上
如果配置了routing_key会被忽略。

消费者同上

设置队列和交换机和绑定
   
    @Bean
    public Queue testQueue() {
    	return new Queue(queueName);
    }
    @Bean
    public Queue testQueue1() {
    	return new Queue(queueName1);
    }
    
    @Bean
    public FanoutExchange testExchange() {
        return new FanoutExchange(exchangeName);
    }
    
    
    @Bean
    public Binding binding() {
    	return BindingBuilder.bind(testQueue()).to(testExchange());
    }
    @Bean
    public Binding binding1() {
    	return BindingBuilder.bind(testQueue1()).to(testExchange());
    }

发送消息
	@Override
	public void run(String... args) throws Exception {
		this.rabbitTemplate.convertAndSend(exchangeName,null,new Date());
	}




附件










参考

springboot和rabbitmq的交换机各个类型的实例
http://blog.csdn.net/u010288264/article/details/55260237






