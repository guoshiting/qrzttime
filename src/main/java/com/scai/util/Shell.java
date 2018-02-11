package com.scai.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.scai.entity.MyUserInfo;
/**
 * 必须多例
 * @author Administrator
 *
 */
public class Shell {

	private final static Logger logger = LoggerFactory.getLogger(Shell.class);
	private String ip;
	private String username;
	private String password;
	private int port;
	private ArrayList<String> stdout = null;
	public static final int DEFAULT_SSH_POST = 22;
	
	/**
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @param port
	 * @param flag 是否需要解密
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public Shell(String ip, String username, String password, int port,boolean flag) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		this(ip, username, password,flag);
		if(port > 0){
			this.port = port;
		}else{
			this.port = DEFAULT_SSH_POST;
		}
	}
	/**
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @param flag 是否需要解密
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public Shell(String ip, String username, String password, boolean flag) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		super();
		this.ip = ip;
		this.username = username;
		if(flag)
			this.password = AESUtils.decode(password);
		else
			this.password = password;
		this.stdout = new ArrayList<String>();
		this.port = DEFAULT_SSH_POST;
		logger.info("ip为:{},username为:{},password为:{}",ip,username,password);
	}
	
	public int execute(final String command){
		int returnCode = 0;
		JSch jsch = new JSch();
		MyUserInfo userInfo = new MyUserInfo();
		try {
			//登录
			Session session = jsch.getSession(username, ip, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setUserInfo((UserInfo) userInfo);
			session.connect(30000);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			
			Channel channel = session.openChannel("exec");
			ChannelExec channelExec = (ChannelExec) channel;
			channelExec.setCommand(command);
			channelExec.setInputStream(null);
			BufferedReader input = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
			channelExec.connect();
			System.out.println("The remote command is :" + command);
			
			String line;
			while((line = input.readLine())!= null){
				stdout.add(line);
			}
			input.close();
			
			if(channelExec.isClosed()){
				returnCode = channelExec.getExitStatus();
			}
			channelExec.disconnect();
			
			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnCode;
	}
	
	public ArrayList<String> getStandardOutput(){
		return stdout;
	}
	/**
	 * 调用shell脚本
	 * @param ip
	 * @param user
	 * @param pw
	 * @param script
	 * @return
	 */
	public List<String> executeScript(String script){
		this.execute(script);
		ArrayList<String> stdut = this.getStandardOutput();
		return stdut;
	}
}

