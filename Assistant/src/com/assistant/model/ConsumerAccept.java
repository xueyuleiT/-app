package com.assistant.model;

import java.io.Serializable;

import com.assistant.utils.Constant;

public class ConsumerAccept implements Serializable {
	int customer_id, serverid;
	String customer_name = "", isaccept = "", ishelp = "no",
			helpacceptname = "", accept = "", datatime = "",
			customer_phone = "", imgpath = "", beizhu = "", saleid = Constant.USERPHONE,
			consultantname = Constant.USERNAME,
			consultantphone = Constant.USERPHONE, building = "",
			voicePath = "";
	boolean select = false;

	public int getServerid() {
		return serverid;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getConsultantname() {
		return consultantname;
	}

	public void setConsultantname(String consultantname) {
		this.consultantname = consultantname;
	}

	public String getConsultantphone() {
		return consultantphone;
	}

	public void setConsultantphone(String consultantphone) {
		this.consultantphone = consultantphone;
	}

	public String getSaleid() {
		return saleid;
	}

	public void setSaleid(String saleid) {
		this.saleid = saleid;
	}

	public String getIshelp() {
		return ishelp;
	}

	public void setIshelp(String ishelp) {
		this.ishelp = ishelp;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_phone() {
		return customer_phone;
	}

	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getIsaccept() {
		return isaccept;
	}

	public void setIsaccept(String isaccept) {
		this.isaccept = isaccept;
	}

	public String getHelpacceptname() {
		return helpacceptname;
	}

	public void setHelpacceptname(String helpacceptname) {
		this.helpacceptname = helpacceptname;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getDatatime() {
		return datatime;
	}

	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}

}
