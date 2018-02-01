
![这里写图片描述](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/message-flow-broker-relay.png?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcm9kX2pvaG4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


简单的代理非常适合入门，但是仅支持STOMP命令的一个子集（例如，没有Ack，收据等），依赖于简单的消息发送循环，并且不适合集群。作为替代，应用程序可以升级到使用全功能的消息代理。

检查您选择的消息代理（例如RabbitMQ， ActiveMQ等）的STOMP文档 ，安装代理，并在启用STOMP支持的情况下运行它。然后在Spring配置中启用STOMP代理中继，而不是简单的代理。



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