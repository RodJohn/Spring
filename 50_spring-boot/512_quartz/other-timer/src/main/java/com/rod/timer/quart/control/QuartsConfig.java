package com.rod.timer.quart.control;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParseException;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.rod.timer.quart.easy.ScheduledTasks;



//@Configuration
public class QuartsConfig {

	
    @Bean  
    public Scheduler scheduler() throws IOException, SchedulerException {  
        SchedulerFactory schedulerFactory = new StdSchedulerFactory(quartzProperties());  
        Scheduler scheduler = schedulerFactory.getScheduler();  
        scheduler.start();  
        return scheduler;  
    }  
    
  
    /** 
     * 设置quartz属性 
     */  
    public Properties quartzProperties() throws IOException {  
        Properties prop = new Properties(); 
        
        prop.put("quartz.scheduler.instanceName", "ServerScheduler");  
        //它用来在用到多个调度器区分特定的调度器实例。多个调度器通常用在集群环境中。
        
        prop.put("org.quartz.scheduler.instanceId", "AUTO"); 
        //设置为 AUTO.在非集群环境中，值将会是 NON_CLUSTERED。在集群环境下，值将会是主机名加上当前的日期和时间
        //prop.put("org.quartz.scheduler.skipUpdateCheck", "true");  
        //prop.put("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory");  
        
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");  
        prop.put("org.quartz.threadPool.threadCount", "5");  
        //原则上是，要处理的 Job 越多，那么需要的工作者线程也就越多。threadCount 的数值至少为 1
        //Quartz 自带的线程池实现类.必须设定
        
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");  
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");  
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");  
        prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");  
        prop.put("org.quartz.dataSource.quartzDataSource.driver", "com.mysql.jdbc.Driver");  
        prop.put("org.quartz.dataSource.quartzDataSource.URL", "jdbc:mysql://192.168.221.53:3306/quartz");  
        prop.put("org.quartz.dataSource.quartzDataSource.user", "web");  
        prop.put("org.quartz.dataSource.quartzDataSource.password", "webserver!123");  
        prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", "10");  
        //prop.put("org.quartz.jobStore.isClustered", "true");  
        //
        
        
        
  
        return prop;  
    }  

}
