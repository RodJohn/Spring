

# 概述

1.1）problem：许多浏览器不支持 WebSocket 协议；
1.2）solutions： SockJS 是 WebSocket 技术的一种模拟。SockJS 会 尽可能对应 WebSocket API，但如果 WebSocket 技术 不可用的话，就会选择另外的 通信方式协议；

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

sockjs，对于低版本的ie等不支持websocket的浏览器，采用js模拟websocket对象的办法来实现兼容（其实也有轮询的情况）

SockJS是为在浏览器中使用而设计的。它使用各种各样的技术支持广泛的浏览器版本。对于SockJS传输类型和浏览器的完整列表，可以看到SockJS客户端页面。 
传输分为3类:WebSocket、HTTP流和HTTP长轮询（按优秀选择的顺序分为3类）

# 向下

SockJS 会优先选择 WebSocket 协议，但是如果 WebSocket协议不可用的话，他就会从如下 方案中挑选最优可行方案：

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