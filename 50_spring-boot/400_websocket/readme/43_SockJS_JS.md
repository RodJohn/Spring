


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