package com.john.rod.boot.websocket.rabbitmq;

import com.john.rod.boot.websocket.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.Map;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@Controller
@Import({Application.WebMvcConfig.class, Application.WebSocketMessageBrokerConfig.class})
public class Application {

    @Autowired SimpMessagingTemplate messagingTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = run(Application.class, args);
    }



    public class WebMvcConfig extends WebMvcConfigurerAdapter{
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("index").setViewName("myindextopic");
        }
    }

    @EnableWebSocketMessageBroker
    public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/chathost").setAllowedOrigins("*").withSockJS();
        }
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableStompBrokerRelay("/topic", "/queue")
                    .setRelayHost("127.0.0.1")
                    .setRelayPort(61613)
                    .setClientLogin("admin")
                    .setClientPasscode("admin");
            registry.setApplicationDestinationPrefixes("/app1","/app2");
        }
    }


    @MessageMapping("/welcome")
//    @SendTo("/topic1/welcome")
    public Message handleMessage(@Payload Message msg , @Headers Map  param){
        return msg;
    }

//    @MessageMapping("/welcome")
//    public void handleMessage(Message msg){
//        messagingTemplate.convertAndSend("/topic1/welcome",msg);
//    }



    @SubscribeMapping("/topic1/welcome")
    public Message handleSubscription1() {
        return new Message("订阅事件--/topic1/welcome");
    }

}