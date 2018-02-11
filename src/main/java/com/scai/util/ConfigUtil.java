package com.scai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigUtil {
	private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	private static final Properties prop = new Properties();
	
	// 用静态代码块
	static {
		
		InputStream isConfig = null;
		InputStream isBatchConfig = null;
		try {
			
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			// batch任务配置
			isConfig = cl.getResourceAsStream("config.properties");
			prop.load(isConfig);
		} catch (Exception e) {
			logger.error("加载批量配置异常!", e);
			throw new Error("加载批量配置异常!");
		} finally {
			
			try {
				if(null != isConfig){
					isConfig.close();
				}
			} catch (IOException e) {
				logger.error("释放批量配置资源异常!", e);
			}
			try {
				if(null != isBatchConfig){
					isBatchConfig.close();
				}
			} catch (IOException e) {
				logger.error("释放批量配置资源异常!", e);
			}
		}
	}
	
	public static String getProValue(String proName) {
		String proValue = prop.getProperty(proName);
		return proValue;
	}
}
