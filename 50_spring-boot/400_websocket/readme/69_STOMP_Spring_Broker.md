
# 消息传播流程


![这里写图片描述](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/message-flow-broker-relay.png?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcm9kX2pvaG4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

可以进入自定义的代理管道
也可以直接进入代理服务器


# 使用内置STOMP服务

enableSimpleBroker默认情况下使用启用的是spring内置的STOMP功能
这仅支持STOMP命令的一个子集（例如，没有Ack，收据等），
依赖于简单的消息发送循环，并且不适合集群。
作为替代，应用程序可以升级到使用全功能的消息代理。



# 使用外部STOMP服务器


## 服务器


检查您选择的消息代理（例如RabbitMQ， ActiveMQ等）的STOMP文档 ，安装代理，并在启用STOMP支持的情况下运行它。STOMP


## 
然后在Spring配置中启用STOMP代理中继，而不是简单的代理。



```
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/portfolio").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

}
```

上述配置中的“STOMP代理中继”是Spring MessageHandler ，它通过将消息转发给外部消息代理来处理消息。
为此，它建立与代理的TCP连接，将所有消息转发给代理，然后通过WebSocket会话将从代理接收到的所有消息转发给客户端。
从本质上讲，它充当了一个“双向”转发的“中继”。


# 使用 STOMP 代理来替换内存代理

```
@Override  
 public void configureMessageBroker(MessageBrokerRegistry registry) {  
  // 启用了 STOMP 代理中继功能，并将其代理目的地前缀设置为 /topic and /queue .  
  registry.enableStompBrokerRelay("/queue", "/topic")  
   .setRelayPort(62623);  
  registry.setApplicationDestinationPrefixes("/app"); // 应用程序目的地.  
 } 
```

A1）方法第一行启用了 STOMP 代理中继功能： 并将其目的地前缀设置为 "/topic" or "/queue" ；spring就能知道 所有目的地前缀为 "/topic" or "/queue" 的消息都会发送到 STOMP 代理中；
A2）方法第二行设置了 应用的前缀为 "app"：所有目的地以 "/app" 打头的消息（发送消息url not 连接url）都会路由到 带有 @MessageMapping 注解的方法中，而不会发布到 代理队列或主题中；


A1）enableStompBrokerRelay() and setApplicationDestinationPrefixes() 方法都可以接收变长 参数；
A2）默认情况下： STOMP 代理中继会假设 代理监听 localhost 的61613 端口，并且 client 的 username 和password 均为 guest。当然你也可以自行定义；

```
@Override  
public void configureMessageBroker(MessageBrokerRegistry registry) {  
registry.enableStompBrokerRelay("/topic", "/queue")  
.setRelayHost("rabbit.someotherserver")  
.setRelayPort(62623)  
.setClientLogin("marcopolo")  
.setClientPasscode("letmein01");  
registry.setApplicationDestinationPrefixes("/app", "/foo");  
} // setXXX()方法 是可选的  
```


# 注意rabbitmq 的特点

destination

# 问题

https://github.com/spring-projects/spring-boot/issues/3459
