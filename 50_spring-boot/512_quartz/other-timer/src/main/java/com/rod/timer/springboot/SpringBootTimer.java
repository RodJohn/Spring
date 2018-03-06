package com.rod.timer.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * SpringBoot定时任务
 * @author rod_john
 */

//@Component 
public class SpringBootTimer {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
    @Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次  
    public void statusCheck() {      
        logger.info("每分钟执行一次");  
    }    
  
    @Scheduled(fixedRate=20000)  
    public void testTasks() {      
        logger.info("每20秒执行一次");  
    }    

}
