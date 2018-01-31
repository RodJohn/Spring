

# 概述

STOMP即Simple (or Streaming) Text Orientated Messaging Protocol，
简单(流)文本定向消息协议，它提供了一个可互操作的连接格式，
允许STOMP客户端与任意STOMP消息代理（Broker）进行交互。

 WebSocket（SockJS） 就很类似于 使用 TCP 套接字来编写 web 应用；因为没有高层协议，因此就需要我们定义应用间所发送消息的语义，还需要确保 连接的两端都能遵循这些语义；
 同 HTTP 在 TCP 套接字上添加 请求-响应 模型层一样，STOMP 在 WebSocket 之上提供了一个基于 帧的线路格式层，用来定义消息语义；（干货——STOMP 在 WebSocket 之上提供了一个基于 帧的线路格式层，用来定义消息语义）



# 格式


STOMP的客户端和服务器之间的通信是通过“帧”（Frame）实现的，每个帧由多“行”（Line）组成。
第一行包含了命令，然后紧跟键值对形式的Header内容。
    CONNECT
    SEND
    SUBSCRIBE
    UNSUBSCRIBE
    BEGIN
    COMMIT
    ABORT
    ACK
    NACK
    DISCONNECT
第二行必须是空行。
第三行开始就是Body内容，末尾都以空字符结尾。
STOMP的客户端和服务器之间的通信是通过MESSAGE帧、RECEIPT帧或ERROR帧实现的，它们的格式相似。


```
SEND  
destination:/app/marco  
content-length:20 

{\"message\":\"Marco!\"}  
```

A1）SEND：STOMP命令，表明会发送一些内容；
A2）destination：头信息，用来表示消息发送到哪里；
A3）content-length：头信息，用来表示 负载内容的 大小；
A4）空行：
A5）帧内容（负载）内容：



