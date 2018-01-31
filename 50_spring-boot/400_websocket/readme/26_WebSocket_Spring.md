

# 原理

spring 的websocket实现，实际上是一个简易版的消息队列（而且是主题-订阅模式的），
对于要发给具体用户的消息，spring的实现是创建了一个跟用户名相关的主题，
实际上如果同一用户登录多个客户端，每个客户端都会收到消息，
因此可以看出来，spring的websocket实现是基于广播模式的，
要实现真正的单客户端用户区分（单用户多端登录只有一个收到消息），只能依赖于session（相当于一个终端一个主题了）。


![这里写图片描述](http://img.blog.csdn.net/20180131172942918?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcm9kX2pvaG4=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



客户端注册到执行url的过程，

实际就是客户端跟服务端建立websocket连接的过程

客户端发送消息，

服务端接收后先判断该消息是否需要经过后台程序处理，也就是是否是application消息（图中的/app分支），
如果是，则根据消息的url路径转到controller中相关的处理方法进行处理，
处理完毕后发送到具体的主题或者队列；

如果不是application消息，
则直接发送到相关主题或者队列，
然后经过处理发送给客户端。


@EnableWebSocketMessageBroker 的作用是在WebSocket 上启用 STOMP，

registerStompEndpoints方法的作用是websocket建立连接用的（也就是所谓的注册到指定的url），
configureMessageBroker方法作用是配置一个简单的消息代理。
如果补充在，默认情况下会自动配置一个简单的内存消息队列，用来处理“/topic”为前缀的消息，
但经过重载后，消息队列将会处理前缀为“/topic”、“/user”的消息，并会将“/app”的消息转给controller处理。


通道

交换机

队列




使用 spring 的低层级 WebSocket API


## WebSocketHandler

为了在 spring 中 使用较低层级的 API 来处理消息。有如下方案：
我们必须编写一个实现 WebSocketHandler

实现 WebSocketHandler

```
public interface WebSocketHandler {  
void afterConnectionEstablished(WebSocketSession session) throws Exception;  
void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception;  
void handleTransportError(WebSocketSession session,  
 Throwable exception) throws Exception;   
void afterConnectionClosed(WebSocketSession session,  
 CloseStatus closeStatus) throws Exception;   
boolean supportsPartialMessages();  
}  
```

扩展 AbstractWebSocketHandler

```
public class ChatTextHandler extends AbstractWebSocketHandler {  
  
 // handle text msg.  
 @Override  
 protected void handleTextMessage(WebSocketSession session,  
   TextMessage message) throws Exception {  
  session.sendMessage(new TextMessage("hello world."));  
 }  
}  
还可以重载其他三个方法
handleBinaryMessage()  
handlePongMessage()  
handleTextMessage()  
```

也可以扩展 TextWebSocketHandler


三者间的关系

TextWebSocketHandler 继承 AbstractWebSocketHandler 


// 当新连接建立的时候，被调用 afterConnectionEstablished  

// 当连接关闭时被调用；afterConnectionClosed


## 配置到 springmvc

WebSocketHandlerRegistry .addHandler() 方法 来注册信息处理器

```
@Configuration  
@EnableWebSocket  
public class WebSocketConfig implements WebSocketConfigurer{  
 @Override  
 public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {  
  registry.addHandler(getTextHandler(), "/websocket/p2ptext");  
 } // 将 ChatTextHandler 处理器 映射到  /websocket/p2ptext 路径下.  
   
 @Bean  
 public ChatTextHandler getTextHandler() {  
  return new ChatTextHandler();  
 }  
}  
```


# 参考 

原理\
https://www.cnblogs.com/nevermorewang/p/7274217.html

http://zk-chs.iteye.com/blog/2285329