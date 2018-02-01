要从浏览器进行连接，对于SockJS，您可以使用 sockjs-client。对于STOMP，许多应用程序都使用了功能完备的jmesnil / stomp-websocket库（也称为stomp.js），已经在生产中使用了多年，但不再进行维护。
目前， JSteunou / webstomp-client是该库最活跃，最新的继承者，下面的示例代码是基于它：


建立连接

var socket = new SockJS('/endpointWisely'); 
var stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {}

连接SockJS的endpoint是“endpointWisely”，与后台代码中注册的endpoint要一样。




```

//1



创建STOMP协议的webSocket客户端。


stompClient.connect({}, function(frame) {//3
    setConnected(true);
    console.log('开始进行连接Connected: ' + frame);
    stompClient.subscribe('/topic/getResponse', function(respnose){ //4
        showResponse(JSON.parse(respnose.body).responseMessage);
    });
});

（3）连接webSocket的服务端。
（4）通过stompClient.subscribe（）订阅服务器的目标是'/topic/getResponse'发送过来的地址，与@SendTo中的地址对应。


（5）通过stompClient.send（）向地址为"/welcome"的服务器地址发起请求，与@MessageMapping里的地址对应。


```


stompClient.send("/app/hello", {}, JSON.stringify({'name':name}))： 第一个参数：json 负载消息发送的 目的地； 第二个参数：是一个头信息的Map，它会包含在 STOMP 帧中；第三个参数：负载消息；
（干货—— stomp client 连接地址 和 发送地址不一样的，连接地址为 <c:url value='/hello'/> ==localhost:8080/springmvc_project_name/hello , 而 发送地址为 '/app/hello'，这里要当心）


