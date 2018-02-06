package com.john.rod.boot.websocket.origin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static org.springframework.boot.SpringApplication.run;

@Component
@ServerEndpoint("/origin")
public class ServerEndPoint {


    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }


    private Session session;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        logger.info("new connection...");
    }

    @OnClose
    public void onClose() throws IOException{
        logger.info("one connection closed...");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("message received: {}", message);
        this.session.getBasicRemote().sendText(message+"是你发的吗?");
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }



}