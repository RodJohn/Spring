package com.rod.timer.quart.easy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component  
@Configurable  
@EnableScheduling  
public class ScheduledTasks {  
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
  
    public void work(){  
    	logger.info("每5秒执行一次");  
    }  
}  