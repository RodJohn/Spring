


WebSocket 客户端的 API 


所有的操作都是采用消息的方式触发的，这样就不会阻塞UI，使得UI有更快的响应时间，得到更好的用户体验。


# 构造

```
var ws = new WebSocket('ws://localhost:8080');
```

WebSocket 对象作为一个构造函数，用于新建 WebSocket 实例
执行上面语句之后，客户端就会与服务器进行连接。

# 属性

readyState

readyState属性返回实例对象的当前状态，共有四种

CONNECTING：值为0，表示正在连接。
OPEN：值为1，表示连接成功，可以通信了。
CLOSING：值为2，表示连接正在关闭。
CLOSED：值为3，表示连接已经关闭，或者打开连接失败。


# webSocket.onopen

实例对象的onopen属性，用于指定连接成功后的回调函数。

```
ws.onopen = function () {
  ws.send('Hello Server!');
}
```

如果要指定多个回调函数，可以使用addEventListener方法。

```
ws.addEventListener('open', function (event) {
  ws.send('Hello Server!');
});
```

# webSocket.onclose


实例对象的onclose属性，用于指定连接关闭后的回调函数。

```
ws.onclose = function(event) {
  var code = event.code;
  var reason = event.reason;
  var wasClean = event.wasClean;
  // handle close event
};

ws.addEventListener("close", function(event) {
  var code = event.code;
  var reason = event.reason;
  var wasClean = event.wasClean;
  // handle close event
});
```

# webSocket.onerror

实例对象的onerror属性，用于指定报错时的回调函数。

```
socket.onerror = function(event) {
  // handle error event
};

socket.addEventListener("error", function(event) {
  // handle error event
});
```

# webSocket.send()


实例对象的send()方法用于向服务器发送数据。

```
发送文本的例子。

ws.send('your message');
发送 Blob 对象的例子。


var file = document
  .querySelector('input[type="file"]')
  .files[0];
ws.send(file);
发送 ArrayBuffer 对象的例子。


// Sending canvas ImageData as ArrayBuffer
var img = canvas_context.getImageData(0, 0, 400, 320);
var binary = new Uint8Array(img.data.length);
for (var i = 0; i < img.data.length; i++) {
  binary[i] = img.data[i];
}
ws.send(binary.buffer);
```

# webSocket.onmessage


实例对象的onmessage属性，用于指定收到服务器数据后的回调函数。

```
ws.onmessage = function(event) {
  var data = event.data;
  // 处理数据
};

ws.addEventListener("message", function(event) {
  var data = event.data;
  // 处理数据
});
```

注意，服务器数据可能是文本，也可能是二进制数据（blob对象或Arraybuffer对象）。

ws.onmessage = function(event){
  if(typeof event.data === String) {
    console.log("Received data string");
  }

  if(event.data instanceof ArrayBuffer){
    var buffer = event.data;
    console.log("Received arraybuffer");
  }
}

除了动态判断收到的数据类型，也可以使用binaryType属性，显式指定收到的二进制数据类型。

```
// 收到的是 blob 数据
ws.binaryType = "blob";
ws.onmessage = function(e) {
  console.log(e.data.size);
};

// 收到的是 ArrayBuffer 数据
ws.binaryType = "arraybuffer";
ws.onmessage = function(e) {
  console.log(e.data.byteLength);
};
```



# 案例

基于原生API的简单例子\
http://blog.csdn.net/u010136741/article/details/51581298\
http://blog.csdn.net/u010136741/article/details/51612594\

