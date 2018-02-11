package com.scai.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) 
public class JobTimingEntity {

	private String job;
	private String jobGroup;
	private String triggerName;
	private String triggerGroup;
	private String corn;
	private String className;
	private String jobStatus;
	private String jobDes;
	
	public String getJobDes() {
		return jobDes;
	}
	public void setJobDes(String jobDes) {
		this.jobDes = jobDes;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getTriggerGroup() {
		return triggerGroup;
	}
	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
	public String getCorn() {
		return corn;
	}
	public void setCorn(String corn) {
		this.corn = corn;
	}
	@Override
	public String toString() {
		return "JobTimingEntity [job=" + job + ", jobGroup=" + jobGroup + ", triggerName=" + triggerName
				+ ", triggerGroup=" + triggerGroup + ", corn=" + corn + ", className=" + className + ", jobStatus="
				+ jobStatus + ", jobDes=" + jobDes + "]";
	}
}
