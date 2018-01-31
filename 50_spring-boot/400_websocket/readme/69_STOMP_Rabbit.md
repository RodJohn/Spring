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