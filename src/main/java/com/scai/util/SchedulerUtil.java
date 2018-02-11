package com.scai.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.scai.entity.JobTimingEntity;

/**
 * 调用任务的类
 */
public class SchedulerUtil {
	private final static String STATUE_EXIT="启用";
	private final static String STATUE_EXEC="任务正在执行";
	private final static String STATUE_NO="未启用";

	// 通过schedulerFactory获取一个调度器 (单例)
	private static SchedulerFactory schedulerfactory = new StdSchedulerFactory();
	private static Map<String,JobTimingEntity> mapJob = new HashMap<String,JobTimingEntity>();

	private static Scheduler scheduler = null;
	
	private static Scheduler getScheduler() throws SchedulerException {
		if(scheduler == null) {
			synchronized (SchedulerUtil.class) {
				if(scheduler == null) {
					scheduler = schedulerfactory.getScheduler();
				}
			}
		}
		return scheduler;
	}

	private SchedulerUtil() {
	}
	public static Map<String, JobTimingEntity> getMapJob() {
		return mapJob;
	}

	public static void setMapJob(Map<String, JobTimingEntity> mapJob) {
		SchedulerUtil.mapJob = mapJob;
	}
	public static void startJob(JobTimingEntity entity) throws Exception {
		 scheduler = getScheduler();
		// 通过schedulerFactory获取一个调度器
		
		// 创建jobDetail实例，绑定Job实现类
		// 指明job的名称，所在组的名称，以及绑定job类
		// JobDetail job = JobBuilder.newJob(TimingShellJob.class).withIdentity("job1",
		// "jgroup1").build();
		JobDetail job = JobBuilder.newJob((Class<? extends Job>) Class.forName(entity.getClassName())).withIdentity(entity.getJob(), entity.getJobGroup())
				.build();
		// 定义调度触发规则
		// Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger",
		// "triggerGroup")
		// .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?
		// ")).startNow().build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(entity.getTriggerName(), entity.getTriggerGroup())
				.withSchedule(CronScheduleBuilder.cronSchedule(entity.getCorn())).startNow().build();

		// 把作业和触发器注册到任务调度中
		scheduler.scheduleJob(job, trigger);

		// 启动调度
		scheduler.start();
	}
	
	/**
	 * 删除任务
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @throws SchedulerException
	 */
	public static void remove(String jobName,String jobGroup) throws SchedulerException{
		Scheduler scheduler = getScheduler();
		JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
		scheduler.deleteJob(jobKey);
	}
	/**
	 * 查看正在执行的任务
	 * @param scheduler
	 * @throws SchedulerException
	 */
	public static List<JobTimingEntity> viewJob() throws SchedulerException{
		Scheduler scheduler = getScheduler();
		List<JobTimingEntity> jobList = new ArrayList<JobTimingEntity>();
		HashSet<String> setJobName = new HashSet<String>();
		Map<String, JobTimingEntity> map = getMapJob();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		for (JobExecutionContext jobExecutionContext : executingJobs) {
			setJobName.add(jobExecutionContext.getJobDetail().getKey().getName());
		}
		for (String claz : map.keySet()) {
			JobTimingEntity jobTimingEntity = map.get(claz);
			JobKey jobKey = new JobKey(jobTimingEntity.getJob(), jobTimingEntity.getJobGroup());
			if(scheduler.checkExists(jobKey)) {
				if(setJobName.contains(jobTimingEntity.getJob())) {
					jobTimingEntity.setJobStatus(STATUE_EXEC);
				}else {
					jobTimingEntity.setJobStatus(STATUE_EXIT);
				}
				jobList.add(jobTimingEntity);
			}else {
				jobTimingEntity.setJobStatus(STATUE_NO);
				jobList.add(jobTimingEntity);
			}
		}
		return jobList;
	}
	
	public static void updateCorn(JobTimingEntity entity) throws SchedulerException {
		CronTrigger build = TriggerBuilder.newTrigger().withIdentity(entity.getTriggerName(), entity.getTriggerGroup())
				.withSchedule(CronScheduleBuilder.cronSchedule(entity.getCorn())).startNow().build();
		scheduler.rescheduleJob(new TriggerKey(entity.getTriggerName(), entity.getTriggerGroup()), build);
	}
}
