package com.scai.listener;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scai.entity.JobTimingEntity;
import com.scai.util.ConfigUtil;
import com.scai.util.SchedulerUtil;

/**
 * 初始化批量任务: 
 * 根据数据库中的批量启用停用状态,执行任务. 并把所有的执行状态改为未执行(主机).
 * @author gst
 *
 */
@Component("BeanDefineConfigue")
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(InitListener.class);
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent ev) {
		
		if (ev.getApplicationContext().getParent() == null) {
			try {
				logger.info("========= 监听方法执行: 定时任务初始化[开始] =========");
				int i = 0;
				ObjectMapper mapper = new ObjectMapper();
				String jobListStr = new String(ConfigUtil.getProValue("JOB.TIMING").getBytes("ISO-8859-1"),"UTF-8");
				logger.info(jobListStr);
				@SuppressWarnings("unchecked")
				ArrayList<Map<String,String>> jogList =mapper.readValue(jobListStr, ArrayList.class);
				for (Map<String,String> map : jogList) {
					JobTimingEntity vo = mapper.readValue(mapper.writeValueAsString(map), JobTimingEntity.class);
					vo.setJob("job"+i);
					vo.setJobGroup("jGroup"+1);
					vo.setTriggerGroup("trigGroup"+i);
					vo.setTriggerName("trigName"+i);
					SchedulerUtil.startJob(vo);
					SchedulerUtil.getMapJob().put(vo.getClassName(), vo);
					i++;
				}
				logger.info("========= 监听方法执行: 批量初始化[结束] =========");
			} catch (Exception e) {
				logger.error("批量初始化出错:", e);
			}
		}
	}
}
