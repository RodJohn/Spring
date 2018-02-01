



# 消息传播过程


![这里写图片描述](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/message-flow-broker-relay.png?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcm9kX2pvaG4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

如图,从客户端发来的消息有两种传播路径
1.自动进入消息代理
2.手动转发

# 自动分发

1.客户端发送消息的destination前缀符合中消息代理定义的应用前缀时,
2.消息将被被直接发送到消息代理
3.消息代理通过destination将消息分发到订阅的客户端

## 示例

定义消息代理前缀

```
public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic1","/topic2");
    }
}
```

客户端发送数据

```
>>> SEND
destination:/topic2/welcome
content-length:13

{"name":"222"}

```

客户端接受到的数据

```
<<< MESSAGE
destination:/topic2/welcome
subscription:sub-1
message-id:bxsl0uvf-1
content-length:14

{"name":"222"}

```


# 手动转发

1.客户端发送的消息destination前缀符合ApplicationDestinationPrefixes中定义的应用前缀时,
2.系统会把destination前缀去除获得目标路径;
3.消息将被路由到和在在控制器中目标路径匹配的@MessageMapping注释的方法中;

    

## 示例

定义手动前缀

```
public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app1","/app2");
    }
}
```

定义注解方法

```
@Controller

@MessageMapping("/welcome")
public Message handleMessage(Message msg){
    return msg;
}
```

客户端发送数据

```
1

>>> SEND
destination:/app1/welcome
content-length:13

{"name":"re"}


2
>>> SEND
destination:/app2/welcome
content-length:14

{"name":"rew"}
```

解释

```
destination为"/app1/welcome"和"/app2/welcome"时;
符合ApplicationDestinationPrefixes中定义的应用前缀;
spring将destination前缀去除获得路径"/welcome";
"/welcome"被路由到控制器中@MessageMapping在注释的控制器的方法;
```



# 消息发送

## SimpMessagingTemplate 

    spring的SimpMessagingTemplate可以直接向消息代理发送消息

```
simpMessagingTemplate.convertAndSend(path, msg);  
```

## @SendTo

    在使用@MessageMapping接收消息之后，
    可以使用@SendTo直接向消息代理，


### 用法

1.在声明@Controller的类中使用
2.@SendTo中写完整的客户端注册的地址
3.所有订阅这个主题的应用都会收到这条消息
4.如果同一个方法的@MessageMapping和@SendTo的地址相同


### 示例

```
@Controller

@MessageMapping("/topic/welcome")
@SendTo("/topic/topicmodel")
public Message handleMessage1(Message msg){
    System.out.println("客户端发消息到---/topic/welcome---"+msg);
    return new Message("服务器通过SendTo发给/topic/topicmodel的消息");
}

```





# 其他 



##  @SubscribeMapping

### 原理
   
   当收到STOMP订阅消息的时候，@SubscribeMapping标记的方法将会触发
   方法返回消息，不过经过代理。


### 示例

```
@Controller

@SubscribeMapping("/topic/topicmodel")
public Message handleSubscription1() {
    return new Message("订阅事件");
}

```


## 消息转换

2）因为我们现在处理的 不是 HTTP，所以无法使用 spring 的 HttpMessageConverter 实现 将负载转换为Shout 对象。
Spring 4.0 提供了几个消息转换器如下：（Attention， 如果是传输json数据的话，定要添加 Jackson jar 包到你的springmvc 项目中，不然连接不会成功的）



