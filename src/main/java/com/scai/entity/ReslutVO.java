package com.scai.entity;

public class ReslutVO {

	private boolean flag;
	private String msg;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "ReslutVO [flag=" + flag + ", msg=" + msg + "]";
	}
}
