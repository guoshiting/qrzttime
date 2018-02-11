package com.scai.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.UserInfo;

public class MyUserInfo implements UserInfo{
	private final static Logger logger = LoggerFactory.getLogger(MyUserInfo.class);
    @Override
    public String getPassphrase() {
        logger.info("getPassphrase");
        return null;
    }
    @Override
    public String getPassword() {
    	logger.info("getPassword");
        return null;
    }
    @Override
    public boolean promptPassword(String s) {
    	logger.info("promptPassword:"+s);
        return false;
    }
    @Override
    public boolean promptPassphrase(String s) {
    	logger.info("promptPassphrase:"+s);
        return false;
    }
    @Override
    public boolean promptYesNo(String s) {
    	logger.info("promptYesNo:"+s);
        return true;
    }
    @Override
    public void showMessage(String s) {
    	logger.info("showMessage:"+s);
    }
}
