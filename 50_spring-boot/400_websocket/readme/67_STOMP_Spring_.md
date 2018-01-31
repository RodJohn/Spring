
# 接收


## 1）借助 @MessageMapping 注解能够 在 控制器中处理 STOMP 消息

A1）@MessageMapping注解：表示 handleShout()方法能够处理 指定目的地上到达的消息；
A2）这个目的地（消息发送目的地url）就是 "/server/app/hello"，其中 "/app" 是 隐含的 ,"/server" 是 springmvc 项目名称；

## 消息转换

2）因为我们现在处理的 不是 HTTP，所以无法使用 spring 的 HttpMessageConverter 实现 将负载转换为Shout 对象。Spring 4.0 提供了几个消息转换器如下：（Attention， 如果是传输json数据的话，定要添加 Jackson jar 包到你的springmvc 项目中，不然连接不会成功的）




# 发送

## 发送消息 @SendTo

如果你想要在接收消息的时候，在响应中发送一条消息，修改方法签名 不是void 类型即可，
消息将会发布到 /topic/greetings， 所有订阅这个主题的应用都会收到这条消息

```
@MessageMapping("/hello")  
    @SendTo("/topic/greetings") //highlight line.  
    public Greeting greeting(HelloMessage message) throws Exception {  
        System.out.println("receiving " + message.getName());  
        System.out.println("connecting successfully.");  
        return new Greeting("Hello, " + message.getName() + "!");  
    }  

```


## 默认

2）默认情况下：帧所发往的目的地会与 触发 处理器方法的目的地相同。所以返回的对象 会写入到 STOMP 帧的负载中，并发布到 "/topic/stomp" 目的地。不过，可以通过 @SendTo 注解，重载目的地；（干货——注解 @SendTo 注解的作用）
代码同上。
对以上代码的分析（Analysis）：消息将会发布到 /topic/hello， 所有订阅这个主题的应用都会收到这条消息；


##  @SubscribeMapping注解
   
   当收到 STOMP 订阅消息的时候，带有 @SubscribeMapping 注解 的方法将会触发；
   其也是通过 AnnotationMethodMessageHandler 来接收消息的；
这个SubscribeMapping annotation标记的方法，是在订阅的时候调用的，也就是说，基本是只执行一次的方法
，client 调用定义在server 的 该 Annotation 标注的方法，它就会返回结果，不过经过代理。

```
  @SubscribeMapping("/macro") // defined in Controller. attention for addr '/macro' in server.  
    public Greeting handleSubscription() {  
        System.out.println("this is the @SubscribeMapping('/marco')");  
        Greeting greeting = new Greeting("i am a msg from SubscribeMapping('/macro').");  
        return greeting;  
    }  

```

## SimpMessagingTemplate 

spring 的 SimpMessagingTemplate 能够在应用的任何地方发送消息，不必以接收一条消息为 前提；

messaging.convertAndSend("/topic/spittlefeed", spittle); // 发送消息.  
对以上代码的分析（Analysis）： 
A1）配置 spring 支持 stomp 的一个附带功能是 在spring应用上下文中已经包含了 Simple
A2）在发布消息给 STOMP 主题的时候，所有订阅该主题的客户端都会收到消息。但有的时候，我们希望将消息发送给指定用户；


## 参考

简单收发
https://spring.io/guides/gs/messaging-stomp-websocket/\



# 指定用户

@MessageMapping and @SubscribeMapping 注解标注的方法 能够使用 Principal 来获取认证用户；

SimpMessagingTemplate 能够发送消息给特定用户；



## @SendToUser

消息目的地有UserDestinationMessageHandler来处理，会将消息路由到发送者对应的目的地。
默认该注解前缀为/user。如：用户订阅/user/hi ，
在@SendToUser('/hi')查找目的地时，会将目的地的转化为/user/{name}/hi, 
这个name就是principal的name值，该操作是认为用户登录并且授权认证，使用principal的name作为目的地标识。
发给消息来源的那个用户。（就是谁请求给谁，不会发给所有用户，区分就是依照principal-name来区分的)。

此外该注解还有个broadcast属性，表明是否广播。就是当有同一个用户登录多个session时，是否都能收到。取值true/false.


## convertAndSendToUser

SimpMessagingTemplate还提供了 convertAndSendToUser() 方法，该方法能够让 我们给特定用户发送消息；





## 客户端

```
stomp.subscribe("/user/queue/notifications", handleNotifications);  
```

这个目的地使用了 "/user" 作为前缀，
在内部，以"/user" 为前缀的消息将会通过 UserDestinationMessageHandler 进行处理，
而不是 AnnotationMethodMessageHandler 或  SimpleBrokerMessageHandler or StompBrokerRelayMessageHandler，

UserDestinationMessageHandler 的主要任务： 是 将用户消息重新路由到 某个用户独有的目的地上。 
在处理订阅的时候，它会将目标地址中的 "/user" 前缀去掉，并基于用户 的会话添加一个后缀。
如，对  "/user/queue/notifications" 的订阅最后可能路由到 名为 "/queue/notifacations-user65a4sdfa" 目的地上；



## 参考 


http://blog.csdn.net/q_an1314/article/details/53640042\
http://blog.csdn.net/u012373815/article/details/54380476\