

# 概述

## 许多浏览器不支持 WebSocket 协议

    一些浏览器中缺少对WebSocket的支持,因此，向下兼容是必须的

## SockJS 模拟 WebSocket 

SockJS是让应用程序在所有现代浏览器和不支持WebSocket协议的环境使用上WebSocket API

提供了一个连贯的、跨浏览器的Javascript API，它在浏览器和web服务器之间创建了一个低延迟、全双工、跨域通信通道。

# 原理

在底层SockJS首先尝试使用本地WebSocket。

如果失败了，它可以使用各种浏览器特定的传输协议，并通过类似WebSocket的抽象方式呈现它们。

会从如下 方案中挑选最优可行方案：

XHR streaming  
XDR streaming  
iFrame event source  
iFrame HTML file  
XHR polling  
XDR polling  
iFrame XHR polling  
JSONP polling  


# 心跳消息
SockJS协议要求服务器发送心跳消息，以阻止代理结束连接。




# 参考

https://github.com/sockjs