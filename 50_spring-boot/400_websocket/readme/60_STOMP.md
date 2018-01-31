

1.1）假设 HTTP 协议 并不存在，只能使用 TCP 套接字来 编写 web 应用，你可能认为这是一件疯狂的 事情；
1.2）不过 幸好，我们有 HTTP协议，它解决了 web 浏览器发起请求以及 web 服务器响应请求的细节；
1.3）直接使用 WebSocket（SockJS） 就很类似于 使用 TCP 套接字来编写 web 应用；因为没有高层协议，因此就需要我们定义应用间所发送消息的语义，还需要确保 连接的两端都能遵循这些语义；
1.4）同 HTTP 在 TCP 套接字上添加 请求-响应 模型层一样，STOMP 在 WebSocket 之上提供了一个基于 帧的线路格式层，用来定义消息语义；（干货——STOMP 在 WebSocket 之上提供了一个基于 帧的线路格式层，用来定义消息语义）



2）STOMP 帧：该帧由命令，一个或多个 头信息 以及 负载所组成。如下就是发送 数据的一个 STOMP帧：（干货——引入了 STOMP帧格式）
[javascript] view plain copy
SEND  
destination:/app/marco  
content-length:20  
  
{\"message\":\"Marco!\"}  
对以上代码的分析（Analysis）：
A1）SEND：STOMP命令，表明会发送一些内容；
A2）destination：头信息，用来表示消息发送到哪里；
A3）content-length：头信息，用来表示 负载内容的 大小；
A4）空行：
A5）帧内容（负载）内容：

3）STOMP帧 信息 最有意思的是 destination头信息了： 它表明 STOMP 是一个消息协议，类似于 JMS 或 AMQP。消息会发送到 某个 目的地，这个 目的地实际上可能真的 有消息代理作为 支撑。另一方面，消息处理器 也可以监听这些目的地，接收所发送过来的消息；


