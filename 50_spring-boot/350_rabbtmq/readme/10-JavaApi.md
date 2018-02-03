

public class MsgSender {  
    private final static String QUEUE_NAME = "hello";  
    public static void main(String[] args) throws IOException {  
        /** 
         * 创建连接连接到MabbitMQ 
         */  
        ConnectionFactory factory = new ConnectionFactory();  
        // 设置MabbitMQ所在主机ip或者主机名  
        factory.setHost("127.0.0.1");  
        // 创建一个连接  
        Connection connection = factory.newConnection();  
        // 创建一个频道  
        Channel channel = connection.createChannel();  
        // 指定一个队列  
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
        // 发送的消息  
        String message = "hello world!龙轩";  
        // 往队列中发出一条消息  
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());  
        System.out.println(" [x] Sent '" + message + "'");  
        // 关闭频道和连接  
        channel.close();  
        connection.close();  
    }  
}  



RabbitMQ不允许使用不同的参数设定重新定义已经存在的队列，并且会向尝试如此做的程序返回一个错误




详细介绍
http://blog.csdn.net/xiaoxian8023/article/details/48679609


http://blog.csdn.net/liaokailin/article/details/49558605