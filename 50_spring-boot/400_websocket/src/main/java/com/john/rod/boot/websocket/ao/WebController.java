package com.john.rod.boot.websocket.ao;

import com.john.rod.boot.websocket.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/data")
    @ResponseBody
    public String handleMessage1(){
        return "msg";
    }

}
