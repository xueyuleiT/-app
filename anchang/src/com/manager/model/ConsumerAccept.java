package com.manager.model;

import java.io.Serializable;

public class ConsumerAccept implements Serializable {
	String customer_id;
	int serverid;
	String customer_name = "", isaccept = "", ishelp = "no",
			helpacceptname = "", accept = "", datatime = "",
			customer_phone = "", imgpath = "", saleid = "",
			consultantname = "", consultantphone = "";

	public int getServerid() {
		return serverid;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
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

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
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
