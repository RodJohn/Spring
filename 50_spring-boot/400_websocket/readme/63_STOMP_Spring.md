


当使用Spring的STOMP支持时，Spring WebSocket应用程序充当客户端的STOMP代理。
消息被路由到@Controller消息处理方法，或路由到一个简单的内存代理，用于跟踪订阅并将消息广播给订阅用户。
您还可以配置Spring以使用专用的STOMP代理（例如RabbitMQ，ActiveMQ等）来实际传播消息。
在这种情况下，Spring维护与代理的TCP连接，将消息转发给它，并将消息传递给连接的WebSocket客户端。
因此，Spring Web应用程序可以依靠统一的基于HTTP的安全性，通用验证以及熟悉的编程模型消息处理工作。




# 优点

使用STOMP作为子协议使得Spring Framework和Spring Security能够提供比使用原始WebSocket更丰富的编程模型。
关于HTTP vs raw TCP以及它如何使Spring MVC和其他Web框架提供丰富的功能，可以做同样的事情。以下是一些好处：

不需要发明自定义消息协议和消息格式。

STOMP客户端 在Spring框架中包括一个Java客户端。

消息代理（如RabbitMQ，ActiveMQ等）可用于（可选）管理订阅和广播消息。

应用程序逻辑可以@Controller根据STOMP目标报头与任何数量的路由到它们的消息进行组织，也可以WebSocketHandler针对给定的连接处理原始的WebSocket消息。

使用Spring Security来保护基于STOMP目标和消息类型的消息。






# 参考

核心资料\
https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket





