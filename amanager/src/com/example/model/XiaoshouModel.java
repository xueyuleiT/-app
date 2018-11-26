package com.example.model;

import java.io.Serializable;

public class XiaoshouModel implements Serializable {
	String name = "", phone = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
