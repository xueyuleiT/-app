package com.assistant.model;

public class CallModel {
	String callTime = "", stopTime = "", phone = "", name = "",
			yixianghuxing = "", yixiang = "", yusuan = "";
	boolean in = false, out = false;

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYixianghuxing() {
		return yixianghuxing;
	}

	public void setYixianghuxing(String yixianghuxing) {
		this.yixianghuxing = yixianghuxing;
	}

	public String getYixiang() {
		return yixiang;
	}

	public void setYixiang(String yixiang) {
		this.yixiang = yixiang;
	}

	public boolean isIn() {
		return in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}

	public boolean isOut() {
		return out;
	}

	public void setOut(boolean out) {
		this.out = out;
	}

	public String getYusuan() {
		return yusuan;
	}

	public void setYusuan(String yusuan) {
		this.yusuan = yusuan;
	}

}
