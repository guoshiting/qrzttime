package com.scai.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scai.entity.JobTimingEntity;
import com.scai.util.SchedulerUtil;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TestJob implements Job {
	
	private final static Logger logger = LoggerFactory.getLogger(TestJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logger.info("任务正在执行!!!!!!!!!!!");
		} catch (Exception e) {
			logger.error("定时任务错误",e);
			logger.info("跑批出错,删除调度任务!");
			JobTimingEntity job = SchedulerUtil.getMapJob().get(this.getClass().toString().replace("class ", ""));
			try {
				SchedulerUtil.remove(job.getJob(), job.getJobGroup());
			} catch (SchedulerException e1) {
				logger.info("任务停止异常,请手动停止",e1);
			}
		}
	}

}
