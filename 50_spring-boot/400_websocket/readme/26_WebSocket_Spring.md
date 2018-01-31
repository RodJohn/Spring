


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


http://zk-chs.iteye.com/blog/2285329