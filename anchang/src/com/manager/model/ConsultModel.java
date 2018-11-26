package com.manager.model;

import java.io.Serializable;

public class ConsultModel implements Serializable {
	int id, count;
	String name, phone, acceptCount = "0", onlineTime, offerlineTime, standOn,
			standOff, helpAcceptCount = "0", state,imgpath;

	public String getState() {
		return state;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStandOn() {
		return standOn;
	}

	public void setStandOn(String standOn) {
		this.standOn = standOn;
	}

	public String getStandOff() {
		return standOff;
	}

	public void setStandOff(String standOff) {
		this.standOff = standOff;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAcceptCount() {
		return acceptCount;
	}

	public void setAcceptCount(String acceptCount) {
		this.acceptCount = acceptCount;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getOfferlineTime() {
		return offerlineTime;
	}

	public void setOfferlineTime(String offerlineTime) {
		this.offerlineTime = offerlineTime;
	}

	public String getHelpAcceptCount() {
		return helpAcceptCount;
	}

	public void setHelpAcceptCount(String helpAcceptCount) {
		this.helpAcceptCount = helpAcceptCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}