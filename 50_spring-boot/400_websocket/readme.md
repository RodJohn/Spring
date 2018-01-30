

# 核心代码

WebSocket 为浏览器和服务器端提供了双工异步通信的功能

建立连接
订阅主题

    new SockJS('/endpointWisely')
    subscribe('/topic/getResponse'

        var socket = new SockJS('/endpointWisely'); //链接SockJS 的endpoint 名称为"/endpointWisely"
        stompClient = Stomp.over(socket);//使用stomp子协议的WebSocket 客户端
        stompClient.connect({}, function(frame) {//链接Web Socket的服务端。
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/getResponse', function(respnose){ //订阅/topic/getResponse 目标发送的消息。这个是在控制器的@SendTo中定义的。
                showResponse(JSON.parse(respnose.body).responseMessage);
            });
        });


开启连接
接受消息

    addEndpoint("/endpointWisely").withSockJS()
    

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //endPoint 注册协议节点,并映射指定的URl

        registry.addEndpoint("/endpointWisely").withSockJS();//注册一个Stomp 协议的endpoint,并指定 SockJS协议。
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(message broker)
        registry.enableSimpleBroker("/topic"); //广播式应配置一个/topic 消息代理

    }


    @MessageMapping("/welcome")
    //浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    @SendTo("/topic/getResponse")
    //服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
    public Response say(Message message) throws Exception {
        Thread.sleep(1000);
        return new Response("Welcome, " + message.getName() + "!");
    }








#  基础项目搭建


## 依赖


## 新建WebSocket 的配置类


## 控制器




# 功能分析

广播式
即服务器段友消息时，会将消息发送给所有链接了当前endpoint的相同主题 的浏览器。



# 参考


http://blog.csdn.net/u012373815/article/details/54375195

https://spring.io/guides/gs/messaging-stomp-websocket/