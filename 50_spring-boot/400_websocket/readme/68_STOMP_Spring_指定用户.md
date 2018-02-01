

# 指定用户

@MessageMapping and @SubscribeMapping 注解标注的方法 能够使用 Principal 来获取认证用户；

SimpMessagingTemplate 能够发送消息给特定用户；



## @SendToUser

消息目的地有UserDestinationMessageHandler来处理，会将消息路由到发送者对应的目的地。
默认该注解前缀为/user。如：用户订阅/user/hi ，
在@SendToUser('/hi')查找目的地时，会将目的地的转化为/user/{name}/hi, 
这个name就是principal的name值，该操作是认为用户登录并且授权认证，使用principal的name作为目的地标识。
发给消息来源的那个用户。（就是谁请求给谁，不会发给所有用户，区分就是依照principal-name来区分的)。

此外该注解还有个broadcast属性，表明是否广播。就是当有同一个用户登录多个session时，是否都能收到。取值true/false.


## convertAndSendToUser

SimpMessagingTemplate还提供了 convertAndSendToUser() 方法，该方法能够让 我们给特定用户发送消息；





## 客户端

```
stomp.subscribe("/user/queue/notifications", handleNotifications);  
```

这个目的地使用了 "/user" 作为前缀，
在内部，以"/user" 为前缀的消息将会通过 UserDestinationMessageHandler 进行处理，
而不是 AnnotationMethodMessageHandler 或  SimpleBrokerMessageHandler or StompBrokerRelayMessageHandler，

UserDestinationMessageHandler 的主要任务： 是 将用户消息重新路由到 某个用户独有的目的地上。 
在处理订阅的时候，它会将目标地址中的 "/user" 前缀去掉，并基于用户 的会话添加一个后缀。
如，对  "/user/queue/notifications" 的订阅最后可能路由到 名为 "/queue/notifacations-user65a4sdfa" 目的地上；



## 参考 


http://blog.csdn.net/q_an1314/article/details/53640042\
http://blog.csdn.net/u012373815/article/details/54380476\




SimpleBroker广播模式

客户端






```
@Configuration
@EnableWebSocketMessageBroker //在 WebSocket 上启用 STOMP
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
 
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //webServer就是websocket的端点，客户端需要注册这个端点进行链接，
        registry.addEndpoint("/webServer").setAllowedOrigins("*").withSockJS();
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setPathMatcher(new AntPathMatcher("."));//可以已“.”来分割路径，看看类级别的@messageMapping和方法级别的@messageMapping
 
        registry.enableSimpleBroker("/topic","/user");
        registry.setUserDestinationPrefix("/user/");
        registry.setApplicationDestinationPrefixes("/app");//走@messageMapping
    }
    
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return true;
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration webSocketTransportRegistration) {
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
    }
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        // TODO Auto-generated method stub
    }
    @Override
    public void addArgumentResolvers(List<org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver> list) {
    }
    @Override
    public void addReturnValueHandlers(List<org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler> list) {
    }
}
        
```


# 参考

https://www.cnblogs.com/nevermorewang/p/7274217.html