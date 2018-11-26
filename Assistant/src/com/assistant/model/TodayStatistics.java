package com.assistant.model;

import java.io.Serializable;

public class TodayStatistics implements Serializable{
	int dayXinFang = 0, dayFuFang = 0, dayYiXiang = 0, dayLaiFang = 0,
			dayYiXiangQiang = 0, dayYiXiangZhong = 0, dayYiXiangYiBan = 0,
			dayDianZhuanFang = 0, dayXinZengRenGou = 0, dayXinZengQianYue = 0;
	String day = "", month = "", phone = "", name = "";
	int dayLaiDian = 0;
	boolean click = false;

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

	public int getDayXinFang() {
		return dayXinFang;
	}

	public void setDayXinFang(int dayXinFang) {
		this.dayXinFang = dayXinFang;
	}

	public int getDayFuFang() {
		return dayFuFang;
	}

	public void setDayFuFang(int dayFuFang) {
		this.dayFuFang = dayFuFang;
	}

	public int getDayYiXiang() {
		return dayYiXiang;
	}

	public void setDayYiXiang(int dayYiXiang) {
		this.dayYiXiang = dayYiXiang;
	}

	public int getDayLaiFang() {
		return dayLaiFang;
	}

	public void setDayLaiFang(int dayLaiFang) {
		this.dayLaiFang = dayLaiFang;
	}

	public int getDayYiXiangQiang() {
		return dayYiXiangQiang;
	}

	public void setDayYiXiangQiang(int dayYiXiangQiang) {
		this.dayYiXiangQiang = dayYiXiangQiang;
	}

	public int getDayYiXiangZhong() {
		return dayYiXiangZhong;
	}

	public void setDayYiXiangZhong(int dayYiXiangZhong) {
		this.dayYiXiangZhong = dayYiXiangZhong;
	}

	public int getDayYiXiangYiBan() {
		return dayYiXiangYiBan;
	}

	public void setDayYiXiangYiBan(int dayYiXiangYiBan) {
		this.dayYiXiangYiBan = dayYiXiangYiBan;
	}

	public int getDayDianZhuanFang() {
		return dayDianZhuanFang;
	}

	public void setDayDianZhuanFang(int dayDianZhuanFang) {
		this.dayDianZhuanFang = dayDianZhuanFang;
	}

	public int getDayXinZengRenGou() {
		return dayXinZengRenGou;
	}

	public void setDayXinZengRenGou(int dayXinZengRenGou) {
		this.dayXinZengRenGou = dayXinZengRenGou;
	}

	public int getDayXinZengQianYue() {
		return dayXinZengQianYue;
	}

	public void setDayXinZengQianYue(int dayXinZengQianYue) {
		this.dayXinZengQianYue = dayXinZengQianYue;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getDayLaiDian() {
		return dayLaiDian;
	}

	public void setDayLaiDian(int dayLaiDian) {
		this.dayLaiDian = dayLaiDian;
	}

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

}
