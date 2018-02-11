package com.scai.job;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scai.entity.JobTimingEntity;
import com.scai.util.AESUtils;
import com.scai.util.ConfigUtil;
import com.scai.util.SchedulerUtil;
import com.scai.util.Shell;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TimingShellJob implements Job {
	
	private final static Logger logger = LoggerFactory.getLogger(TimingShellJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		boolean flag = false;
		try {
			String ip = ConfigUtil.getProValue("SHELL.IP");
			String user = ConfigUtil.getProValue("SHELL.USER");
			String pw =ConfigUtil.getProValue("SHELL.PASSWORD");
			String script = ConfigUtil.getProValue("SHELL.SCRIPT");
			Shell shell= new Shell(ip, user, pw,true);
			List<String> strList = shell.executeScript(script);
			for (String string : strList) {
				logger.info(string);
			}
		} catch (InvalidKeyException e) {
			logger.error("密码错误:",e);
			flag = true;
		} catch (NoSuchAlgorithmException e) {
			logger.error("密码错误:",e);
			flag = true;
		} catch (NoSuchPaddingException e) {
			logger.error("密码错误:",e);
			flag = true;
		} catch (IllegalBlockSizeException e) {
			logger.error("密码错误:",e);
			flag = true;
		} catch (BadPaddingException e) {
			logger.error("密码错误:",e);
			flag = true;
		} catch (Exception e) {
			logger.error("跑批错误:",e);
			flag = true;
		}finally {
			if(flag) {
				logger.info("跑批出错,删除调度任务!");
				JobTimingEntity job = SchedulerUtil.getMapJob().get(this.getClass().toString().replace("class ", ""));
				try {
					SchedulerUtil.remove(job.getJob(), job.getJobGroup());
				} catch (SchedulerException e) {
					logger.info("任务停止异常,请手动停止",e);
				}
			}
		}
	}
}
