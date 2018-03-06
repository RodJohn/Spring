package com.rod.timer.quart.easy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParseException;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;



@Configuration
public class BaseQuartsConfig {

	
    @Bean(name = "detailFactoryBean")  
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(ScheduledTasks scheduledTasks){  
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean ();  
        //这儿设置对应的Job对象  
        bean.setTargetObject (scheduledTasks);  
        //这儿设置对应的方法名  与执行具体任务调度类中的方法名对应  
        bean.setTargetMethod ("work");  
        //设为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始。
        bean.setConcurrent (false);  
        return bean;  
    }  
  
    
    //<!-- 调度触发器 -->
    //<!--触发器的bean的设置，在这里我们设置了我们要触发的jobDetail是哪个。
    //这里我们定义了要触发的jobDetail是searchEngerneTask，即触发器去触发哪个bean。并且我们还定义了触发的时间。
	//spring版本<3.1，quartz版本为1.x，class使用CronTriggerBean；spring版本>3.1，quartz版本为2.x，class使用CronTriggerFactoryBean-->
    @Bean(name = "cronTriggerBean")  
    public CronTriggerFactoryBean cronTriggerBean(MethodInvokingJobDetailFactoryBean detailFactoryBean){  
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean ();  
        trigger.setJobDetail (detailFactoryBean.getObject ());  
        try {  
            trigger.setCronExpression ("0/5 * * ? * *");//每5秒执行一次  
        } catch (ParseException e) {  
            e.printStackTrace ();  
        }  
        return trigger;  
  
    }  
  
       
    //<!-- 调度工厂 -->
    //<!--管理触发器的总设置,管理我们的触发器列表,可以在bean的list中放置多个触发器。 -->
    @Bean  public SchedulerFactoryBean schedulerFactory(CronTriggerFactoryBean cronTriggerBean){  
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean ();  
        schedulerFactory.setTriggers(cronTriggerBean.getObject());  
        return schedulerFactory;  
    }  
       
}
