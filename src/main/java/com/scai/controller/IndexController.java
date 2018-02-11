package com.scai.controller;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scai.entity.JobTimingEntity;
import com.scai.entity.ReslutVO;
import com.scai.service.impl.JobStateServiceImpl;
import com.scai.util.SchedulerUtil;
@Controller
public class IndexController {

	private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private JobStateServiceImpl jobService;
	
	@RequestMapping("index")
	public String getIndex(Model model) {
		logger.info("index方法开始执行");
		try {
			List<JobTimingEntity> jobList = jobService.getJob();
			model.addAttribute("jobList", jobList);
			logger.info(jobList.toString());
		} catch (Exception e) {
			logger.error("获取正在运行的定时任务出错:", e);
		}
		return "index";
	}
	@RequestMapping("stopJob")
	@ResponseBody
	public Object stop(String key) {
		logger.info("stop方法开始执行,key{}",key);
		ReslutVO vo = new ReslutVO();
		try {
			JobTimingEntity job = SchedulerUtil.getMapJob().get(key);
			SchedulerUtil.remove(job.getJob(), job.getJobGroup());
			vo.setFlag(true);
		} catch (SchedulerException e) {
			logger.error("任务停止失败", e);
			vo.setFlag(true);
			vo.setMsg("任务停止失败");
		}
		return vo;
	}
	@RequestMapping("startJob")
	@ResponseBody
	public ReslutVO start(String key) {
		logger.info("start方法开始执行,key为{}",key);
		ReslutVO vo = new ReslutVO();
		try {
			JobTimingEntity job = SchedulerUtil.getMapJob().get(key);
			SchedulerUtil.startJob(job);
			vo.setFlag(true);
		} catch (Exception e) {
			logger.error("定时任务启用失败",e);
			vo.setFlag(true);
			vo.setMsg("任务启用失败");
		}
		return vo;
	}
	@RequestMapping("updatCorn")
	@ResponseBody
	public ReslutVO updateCorn(String corn,String key) {
		logger.info("updateCorn方法开始执行,corn为{},key为{}",corn,key);
		ReslutVO vo = new ReslutVO();
		try {
			JobTimingEntity job = SchedulerUtil.getMapJob().get(key);
			job.setCorn(corn);
			SchedulerUtil.updateCorn(job);
			vo.setFlag(true);
		} catch (SchedulerException e) {
			logger.error("修改corn表达式错误",e);
			vo.setFlag(false);
			vo.setMsg("修改corn表达式出错!");
		}
		return vo;
	}
}
