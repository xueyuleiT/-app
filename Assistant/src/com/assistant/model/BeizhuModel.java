package com.assistant.model;

import java.io.Serializable;

public class BeizhuModel implements Serializable{
	String beizhu = "", time = "";

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
