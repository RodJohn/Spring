package com.rod.timer.quart.control;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobCon1 implements Job{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		logger.info("Hello quzrtz  "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
	}

}
