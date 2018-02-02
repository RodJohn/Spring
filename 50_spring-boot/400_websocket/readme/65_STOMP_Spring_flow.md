



# 消息传播


![这里写图片描述](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/message-flow-broker-relay.png?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcm9kX2pvaG4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

brokerchannel 代理管道


如图,从客户端发来的消息先进入入口管道
之后有两种传播路径
1.自动进入消息代理
2.代理管道
3.不符合任何前缀呢

# 自动分发

1.客户端发送消息的destination前缀符合中消息代理定义的应用前缀时,
2.消息将被被直接发送到消息代理
3.消息代理通过destination将消息通过出口管道分发到订阅的客户端

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


# 代理管道


过程说明

    1.客户端发送的消息destination前缀符合ApplicationDestinationPrefixes中定义的应用前缀时,
    2.系统会把destination前缀去除获得lookupDestination;
    3.消息将被路由到和在在控制器中目标路径匹配的@MessageMapping注释的方法中;


    



## 定义代理管道前缀

```
public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app1","/app2");
    }
}
```

    如上配置,
    输入管道中的SEND消息
    如果destination前缀为"/app1"或者"/app2"
    则将进入代理管道
    将destination的前缀去除(即去除"/app1"或者"/app2")获得lookupDestination


## 定义消息映射方法


    消息映射由@MessageMapping定义
    @MessageMapping注释的方法必须在@Controller注释的类中


### 路径映射

    @MessageMapping用法类似@RequestMapping

    1.类和方法上都可以使用MessageMapping限定路径
    效果为类上值连接方法上值
    2.方法中可以使用路径值,
    在方法中可以通过@DestinationVariable获取路径值
    

```
@Controller

@MessageMapping("/foo")
public class FooController {

    @MessageMapping("/bar/{baz}")
    public void handleBaz(@DestinationVariable String baz) {
        // ...
    }
}

实际的拦截路径为 /foo/bar/1

```

### 解析消息

    消息一般包含负载和头信息,
    在方法参数列表中可以使用下面的注解
    
```
@Payload
    用于访问消息的有效载荷的注释参数，
    用org.springframework.messaging.converter.MessageConverter。
    注释的存在是不需要的，因为它是默认的。

@Header
    访问一个特定的头值
    使用org.springframework.core.convert.converter.Converter 

@Headers
    访问消息中的所有标题
    一般用java.util.Map接受

@DestinationVariable 
    用于访问从消息目标中提取的模板变量的注释参数。
    值将根据需要转换为声明的方法参数类型。

java.security.Principal 
    方法参数反映用户在WebSocket HTTP握手时登录。    

```        


### 转发

@MessageMapping注释的方法可以继续将消息传递下去


#### SimpMessagingTemplate 

    spring的SimpMessagingTemplate可以直接向消息代理发送消息

```
simpMessagingTemplate.convertAndSend(path, msg);  
```

#### 方法返回值

    方法的返回值会被当做消息负载直接发送给消息代理
    默认的Destination是"topic"(写死)+lookupDestination
    可以使用@SendTo重新指定Destination，


#### 示例

```
@MessageMapping("/foo")
public class FooController {

    @MessageMapping("/welcome")
    @SendTo("/topic1/topicmodel")
    public Message handleMessage1(@PayloadMessage msg,@Headers Map param){
        return new Message("服务器通过SendTo发给/topic/topicmodel的消息");
    }
}    

```

    



### 客户端发送数据

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









# 其他 




## 消息转换

2）因为我们现在处理的 不是 HTTP，所以无法使用 spring 的 HttpMessageConverter 实现 将负载转换为Shout 对象。
Spring 4.0 提供了几个消息转换器如下：（Attention， 如果是传输json数据的话，定要添加 Jackson jar 包到你的springmvc 项目中，不然连接不会成功的）



