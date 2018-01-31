

# 概述


二、产生的原因
一些浏览器中缺少对WebSocket的支持,因此，回退选项是必要的，而Spring框架提供了基于SockJS协议的透明的回退选项。

SockJS的一大好处在于提供了浏览器兼容性。优先使用原生WebSocket，如果在不支持websocket的浏览器中，会自动降为轮询的方式。 


SockJS是一个浏览器JavaScript库，它提供了一个类似于网络的对象。SockJS提供了一个连贯的、跨浏览器的Javascript API，它在浏览器和web服务器之间创建了一个低延迟、全双工、跨域通信通道。



SockJS是一个JavaScript库，提供跨浏览器JavaScript的API，

SockJS是一个浏览器JavaScript库，提供了一个类似WebSocket的对象。SockJS为您提供了一个连贯的，跨浏览器的JavaScript API，在浏览器和Web服务器之间创建了一个低延迟，全双工，跨域的通信通道。

在底层SockJS首先尝试使用本地WebSocket。如果失败了，它可以使用各种浏览器特定的传输协议，并通过类似WebSocket的抽象方式呈现它们。

SockJS旨在适用于所有现代浏览器和不支持WebSocket协议的环境，例如，在限制性企业代理之后。

SockJS的一大好处在于提供了浏览器兼容性。优先使用原生WebSocket，如果在不支持websocket的浏览器中，会自动降为轮询的方式。


SockJS的目标是让应用程序使用WebSocket API，但在运行时需要在必要时返回到非WebSocket替代，即无需更改应用程序代码。

SockJS是为在浏览器中使用而设计的。它使用各种各样的技术支持广泛的浏览器版本。对于SockJS传输类型和浏览器的完整列表，可以看到SockJS客户端页面。 
传输分为3类:WebSocket、HTTP流和HTTP长轮询（按优秀选择的顺序分为3类）


# 基础API

A1）SockJS 所处理的URL 是 "http://" 或 "https://" 模式，而不是 "ws://" or  "wss://" ；
A2）其他的函数如 onopen, onmessage, and  onclose ，SockJS 客户端与 WebSocket 一样；

```
 var sock = new SockJS('https://mydomain.com/my_prefix');
 sock.onopen = function() {
     console.log('open');
     sock.send('test');
 };

 sock.onmessage = function(e) {
     console.log('message', e.data);
     sock.close();
 };

 sock.onclose = function() {
     console.log('close');
 };
```



四、心跳消息
SockJS协议要求服务器发送心跳消息，以阻止代理结束连接。




# 参考

https://github.com/sockjs