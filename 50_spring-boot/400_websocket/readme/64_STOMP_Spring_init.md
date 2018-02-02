


# 配置STOMP


```
@Configuration  
@EnableWebSocketMessageBroker  
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {  
  @Override  
  public void registerStompEndpoints(StompEndpointRegistry registry) {  
      registry.addEndpoint("/hello").withSockJS();  
  }  
}  
```

## @EnableWebSocketMessageBroker

    在 WebSocket 上启用 STOMP代理
    spring 的消息功能是基于消息代理构建的


## registerStompEndpoints

    注册STOMP端点
    客户端在订阅或发布消息 到目的地址前，要连接该端点，







#  @SubscribeMapping 

### 原理
   
   当收到STOMP订阅消息的时候，@SubscribeMapping标记的方法将会触发
   方法返回消息，不过经过代理。

    一个@SubscribeMapping注解可以用来映射订阅请求 @Controller的方法。
    它在方法级别上受支持，但也可以与@MessageMapping在同一控制器内的所有消息处理方法中表示共享映射的类型级别注释组合使用。
    默认情况下，@SubscribeMapping方法的返回值作为消息直接发送回连接的客户端，不通过代理。
    这对于实现请求 - 回复消息交互是有用的; 例如，在应用程序UI被初始化时获取应用程序数据。
    或者，@SubscribeMapping也可以用一个方法进行注释，@SendTo 在这种情况下，会将结果消息发送到"brokerChannel"使用指定的目标目标

### 示例

```
@Controller

@SubscribeMapping("/topic/topicmodel")
public Message handleSubscription1() {
    return new Message("订阅事件");
}

```
https://jira.spring.io/browse/SPR-14077