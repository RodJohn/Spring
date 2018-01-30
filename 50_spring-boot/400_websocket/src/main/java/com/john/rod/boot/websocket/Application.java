package com.john.rod.boot.websocket;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        ConfigurableApplicationContext run = run(Application.class, args);
    }


    @MessageMapping("/welcome")
    //浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    @SendTo("/topic/getResponse")
    //服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
    public Response say(Message message) throws Exception {
        Thread.sleep(1000);
        return new Response("Welcome, " + message.getName() + "!");
    }



    @RequestMapping("/")
    public ModelAndView ws() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ws");
        return modelAndView;
    }


    public class Message {
        private String name;

        public String getName() {
            return name;
        }
    }


    public class Response {
        public void setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
        }

        private String responseMessage;

        public Response(String responseMessage) {
            this.responseMessage = responseMessage;
        }

        public String getResponseMessage() {
            return responseMessage;
        }
    }


}