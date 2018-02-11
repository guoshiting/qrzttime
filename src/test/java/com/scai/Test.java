package com.scai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scai.entity.JobTimingEntity;
import com.scai.job.TimingShellJob;
import com.scai.util.AESUtils;
import com.scai.util.ConfigUtil;

public class Test {

	public static void main(String[] args) {
		try {
			SchedulerFactory schedulerfactory = new StdSchedulerFactory();
			Scheduler scheduler = null;
			// 通过schedulerFactory获取一个调度器
			scheduler = schedulerfactory.getScheduler();
			// 创建jobDetail实例，绑定Job实现类
			// 指明job的名称，所在组的名称，以及绑定job类
			 JobDetail job = JobBuilder.newJob(TimingShellJob.class).withIdentity("job1","jgroup1").build();
			// 定义调度触发规则
			 Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger",
			 "triggerGroup").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? ")).startNow().build();

			// 把作业和触发器注册到任务调度中
			scheduler.scheduleJob(job, trigger);

			// 启动调度
			scheduler.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@org.junit.Test
	public void testObj2Json() throws JsonProcessingException {
		JobTimingEntity vo = new JobTimingEntity();
		vo.setCorn("0/5 * * * * ? ");
		vo.setClassName("com.scai.job.TimingShellJob");
		List<JobTimingEntity> list = new ArrayList<JobTimingEntity>();
		list.add(vo);
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(list);
		System.out.println(str);
	}
	@org.junit.Test
	public void testJosn2Obj() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		//String str = ConfigUtil.getProValue("JOB.TIMING");
		String str = "[{\"corn\":\"0/5 * * * * ? \",\"className\":\"com.scai.job.TimingShellJob\"}]";
		ArrayList<Map<String,String>> obj = mapper.readValue(str, ArrayList.class);
		System.out.println(obj.get(0).toString());
	}
	@org.junit.Test
	public void print() throws Exception {
		String jobListStr = ConfigUtil.getProValue("JOB.TIMING");
		
		System.out.println(new String(jobListStr.getBytes("ISO-8859-1"),"UTF-8"));
//		File file = new File("C:\\Users\\Administrator\\Desktop\\config.properties");
//		byte[] filecontent = new byte[(int)file.length()];  
//		FileInputStream in = new FileInputStream(file);
//		in.read(filecontent);
//		String string = new String(filecontent);
//		System.out.println(string);
//		in.close();
	}
	@org.junit.Test
	public void AESUtilTest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String str = AESUtils.encode("rh");
		String string = AESUtils.decode("E3E5B3A9575B52AC87BDA9F525183DC91E928D6CB3A4749DC63809E49736FD97");
		System.out.println(str);
		System.out.println(string);
	}
}
