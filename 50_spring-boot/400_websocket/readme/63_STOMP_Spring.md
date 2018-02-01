





```
@Configuration  
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chathost").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
    }
}
```


## @EnableWebSocketMessageBroker 

在 WebSocket 上启用 STOMP消息代理


## registerStompEndpoints

注册连接的端点

## configureMessageBroker


默认情况下
    会自动配置一个简单的内存消息代理，用来处理 "/topic" 为前缀的消息
    相当于


重载了 configureMessageBroker() 方法：配置了一个 简单的消息代理。
如果不重载，默认case下，。
但经过重载后，消息代理将会处理前缀为 "/topic" and "/queue" 消息。


之外：发送应用程序的消息将会带有 "/app" 前缀，下图展现了 这个配置中的 消息流；



A1）应用程序的目的地 以 "/app" 为前缀，而代理的目的地以 "/topic" 和 "/queue" 作为前缀；
A2）以应用程序为目的地的消息将会直接路由到 带有 @MessageMapping 注解的控制器方法中；(干货—— @MessageMapping的作用)
A3）而发送到 代理上的消息，包括 @MessageMapping注解方法的返回值所形成的消息，将会路由到 代理上，并最终发送到 订阅这些目的地客户端；
（干货——client 连接地址和 发送地址是不同的，以本例为例，前者是/server/hello, 后者是/server/app/XX，先连接后发送）



![这里写图片描述](http://img.blog.csdn.net/20180131144953495?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcm9kX2pvaG4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)![这里写图片描述](http://img.blog.csdn.net/20180117101907667?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcm9kX2pvaG4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



# 





