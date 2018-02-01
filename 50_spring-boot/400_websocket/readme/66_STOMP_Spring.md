
当使用Spring的STOMP支持时，Spring WebSocket应用程序充当客户端的STOMP代理。
消息被路由到@Controller消息处理方法，或路由到一个简单的内存代理，用于跟踪订阅并将消息广播给订阅用户。
您还可以配置Spring以使用专用的STOMP代理（例如RabbitMQ，ActiveMQ等）来实际传播消息。
在这种情况下，Spring维护与代理的TCP连接，将消息转发给它，并将消息传递给连接的WebSocket客户端。
因此，Spring Web应用程序可以依靠统一的基于HTTP的安全性，通用验证以及熟悉的编程模型消息处理工作。




4.4.2。优点
使用STOMP作为子协议使得Spring Framework和Spring Security能够提供比使用原始WebSocket更丰富的编程模型。关于HTTP vs raw TCP以及它如何使Spring MVC和其他Web框架提供丰富的功能，可以做同样的事情。以下是一些好处：

不需要发明自定义消息协议和消息格式。

STOMP客户端 在Spring框架中包括一个Java客户端。

消息代理（如RabbitMQ，ActiveMQ等）可用于（可选）管理订阅和广播消息。

应用程序逻辑可以@Controller根据STOMP目标报头与任何数量的路由到它们的消息进行组织，也可以WebSocketHandler针对给定的连接处理原始的WebSocket消息。

使用Spring Security来保护基于STOMP目标和消息类型的消息。






```
@Configuration  
@EnableWebSocketMessageBroker  
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {  
  @Override  
  public void configureMessageBroker(MessageBrokerRegistry config) {  
      config.enableSimpleBroker("/topic", "/queue");  
      config.setApplicationDestinationPrefixes("/app");  
      // 应用程序以 /app 为前缀，而 代理目的地以 /topic 为前缀.  
      // js.url = "/spring13/app/hello" -> @MessageMapping("/hello") 注释的方法.  
  }  
  
  @Override  
  public void registerStompEndpoints(StompEndpointRegistry registry) {  
      registry.addEndpoint("/hello").withSockJS();  
      // 在网页上我们就可以通过这个链接 /server/hello ==<c:url value='/hello'></span> 来和服务器的WebSocket连接  
  }  
}  
```

## 

@EnableWebSocketMessageBroker 注解的作用： 能够在 WebSocket 上启用 STOMP）
spring 的消息功能是基于消息代理构建的

## registerStompEndpoints

它重载了 registerStompEndpoints() 方法：
将 "/hello" 路径 注册为 STOMP 端点。这个路径与之前发送和接收消息的目的路径有所不同， 
这是一个端点，客户端在订阅或发布消息 到目的地址前，要连接该端点，
即 用户发送请求 url='/server/hello' 与 STOMP server 进行连接，之后再转发到 订阅url；（server== name of your springmvc project ）

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









