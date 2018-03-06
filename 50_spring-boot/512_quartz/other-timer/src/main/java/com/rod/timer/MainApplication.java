package com.rod.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.rod.timer.quart.control.JobManage;


@SpringBootApplication
@EnableScheduling  
@ComponentScan(basePackages="com.rod.timer.quart.control")
public class MainApplication {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApplication.class, args);
		JobManage.startSchedule();
	}
	
}
