package com.manager.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ResponseModel {
	public int msgSize = 0;
	public static HashMap<Integer, UserInfo> userArr = null;
	public	ArrayList<TextMessage> msgArr = new ArrayList<TextMessage>();
	public int getMsgSize() {
		return msgSize;
	}
	public void setMsgSize(int msgSize) {
		this.msgSize = msgSize;
	}
	public ArrayList<TextMessage> getMsgArr() {
		return msgArr;
	}
	public void setMsgArr(ArrayList<TextMessage> msgArr) {
		this.msgArr = msgArr;
	}
	
	
}
