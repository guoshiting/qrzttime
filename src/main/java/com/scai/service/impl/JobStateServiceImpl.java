package com.scai.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.scai.entity.JobTimingEntity;
import com.scai.service.JobStateService;
import com.scai.util.SchedulerUtil;

@Service
public class JobStateServiceImpl implements JobStateService {

	private final static Logger logger = LoggerFactory.getLogger(JobStateServiceImpl.class);

	@Override
	public List<JobTimingEntity> getJob() throws SchedulerException {
		logger.info("getJob方法执行");
		return SchedulerUtil.viewJob();
	}

}
