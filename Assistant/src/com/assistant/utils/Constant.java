package com.assistant.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.assistant.model.ConsumerModel;

public class Constant {
	public static ArrayList<ConsumerModel> conLst = new ArrayList<ConsumerModel>();
	public static String tellPhones[][] = new String[][] {};
	public static int scWidth,scHeight;
	public static String task = "0";
	public static String finishTask = "";
	public static boolean isNeedRefresh = true,isLoading = false;
//	public static String USERID = "";
	public static String USERNAME = "";
	public static String USERPHONE = "";
	public static String SAFEPWD = "";
	public static String MANAGER = "";
	public static String QUYU = "";
//	public static boolean OFFERLINE = true;
	public static String dayNum = "0";
	public static String RUNPAUSE = "RUNSTART";
	public static String CONSUMERIMAGE = "consumer";
	public static String REGISTER = "http://1.anchang.sinaapp.com/Register?action=Register";
	public static String ISLAOYEZHU = "http://1.anchang.sinaapp.com/DataServletTwo?action=islaoyezhu";
	public static String UNREGISTER = "http://1.anchang.sinaapp.com/DataServlet?action=unRegister";
	public static String INSERTCALL = "http://1.anchang.sinaapp.com/DataServlet?action=insertcall";
	public static String ISREGISTER = "http://1.anchang.sinaapp.com/Register?action=isRegister";
	public static String LOGIN = "http://1.anchang.sinaapp.com/LoginSevlet";
	public static String GETDAYDETAIL = "http://1.anchang.sinaapp.com/DataServlet?action=getdaydetail";
	public static String GETCALLRECORD = "http://1.anchang.sinaapp.com/DataServletTwo?action=getcallrecord";
	public static String GETFINISHTASK = "http://1.anchang.sinaapp.com/DataServletTwo?action=getfinishtask";
	public static String GETLAODAIXIN = "http://1.anchang.sinaapp.com/DataServletTwo?action=getlaodaixin";
	public static String GETALLBEIZHU = "http://1.anchang.sinaapp.com/DataServletTwo?action=getallbeizhu";
	public static String GETONECONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=getoneconsumermodel";
	public static String GETCONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=getconsumermodel";
	public static String EXIT = "http://1.anchang.sinaapp.com/coreServlet"+"?action=exit&id=";
	public static String FASTUPDATACONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=fastupdataconsumermodel";
	public static String UPDATACONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=updataconsumermodel";
	
	
	public static boolean isCanUseSim(Context context) { 
	    try { 
	    	
	    	TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
	        if (TelephonyManager.SIM_STATE_READY != mTelephonyManager.getSimState())   
	        {  
	         Toast.makeText(context, "请确认sim卡是否插入或者sim卡暂时不可用！", 0).show();
	         return false;
	        } 
	    	
	        TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
	 
	        return TelephonyManager.SIM_STATE_READY == mgr 
	                .getSimState(); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
	} 

}
