package com.scai.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.scai.entity.JobTimingEntity;

public interface JobStateService {

	public List<JobTimingEntity> getJob() throws SchedulerException;
}
