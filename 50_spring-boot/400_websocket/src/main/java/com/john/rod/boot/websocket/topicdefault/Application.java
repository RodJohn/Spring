package com.john.rod.boot.websocket.topicdefault;

import com.john.rod.boot.websocket.Message;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@Controller
@Import({Application.WebMvcConfig.class, Application.WebSocketMessageBrokerConfig.class})
public class Application {


    public static void main(String[] args) {
        ConfigurableApplicationContext run = run(Application.class, args);
    }



    public class WebMvcConfig extends WebMvcConfigurerAdapter{
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("index").setViewName("myindex");
        }
    }

    @EnableWebSocketMessageBroker
    public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/chathost").withSockJS();
        }
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
//            registry.enableSimpleBroker("/topic");
        }
    }

    @SubscribeMapping("/topic/topicmodel")
    public Message handleSubscription1() {
        System.out.println("订阅--/topic/topicmodel");
        return new Message("订阅事件--1");
    }

    @MessageMapping("/topic/welcome")
    @SendTo("/topic/topicmodel")
    public Message handleMessage1(Message msg){
        System.out.println("客户端发消息到---/topic/topicmodel---"+msg);
        return new Message("服务器通过SendTo发给/topic/topicmodel的消息");
    }


}