package com.john.rod.boot.websocket.ao;

import com.john.rod.boot.websocket.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebController {

    @SubscribeMapping("/topic/topicmodel")
    public Message handleSubscription() {

        return new Message("订阅事件--");
    }

    @MessageMapping("/welcome")
    public void handleMessage(Message msg){
        System.out.println(msg);
    }
}
