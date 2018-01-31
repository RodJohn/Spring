

spring也对socketJS提供了支持。
registry.addEndpoint("/coordination").withSockJS();



SockJS 很容易通过 Java 配置启用


// withSockJS() 方法声明我们想要使用 SockJS 功能，如果WebSocket不可用的话，会使用 SockJS；  

```
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/myHandler").withSockJS();
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }

}
```



四、心跳消息
SockJS协议要求服务器发送心跳消息，以阻止代理结束连接。

Spring SockJS配置有一个名为“心脏节拍时间”的属性，可用于定制频率。默认情况下，如果没有在该连接上发送其他消息，则会在25秒后发送心跳。

当在websocket/SockJS中使用STOMP时，如果客户端和服务器通过协商来交换心跳，那么SockJS的心跳就会被禁用。

Spring SockJS支持还允许配置task调度程序来调度心跳任务。



五、Servlet 3异步请求
HTTP流和HTTP长轮询SockJS传输需要一个连接保持比平常更长时间的连接。 
在Servlet容器中，这是通过Servlet 3的异步支持完成的，这允许退出Servlet的容器线程处理一个请求，并继续从另一个线程中写入响应。

六、SockJS的CROS Headers
如果允许跨源请求，那么SockJS协议使用CORS在XHR流和轮询传输中跨域支持。