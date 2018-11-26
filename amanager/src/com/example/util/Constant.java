package com.example.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.amanager.R;
import com.example.model.ConsumerModel;
import com.example.model.XiaoshouModel;

public class Constant {
	public static ArrayList<ConsumerModel> conLst = new ArrayList<ConsumerModel>();
	public static ArrayList<XiaoshouModel> xiaoshouLst = new ArrayList<XiaoshouModel>();
	public static String tellPhones[][] = new String[][] {};
	public static int scWidth,scHeight;
	public static boolean isNeedRefresh = true,isLoading = false;
	public static String USERID = "";
	public static String USERNAME = "";
	public static String USERPHONE = "";
	public static String SAFEPWD = "";
	public static String MANAGER = "";
	public static String QUYU = "";
	public static String TASK = "";
	public static boolean OFFERLINE = true;
	public static String RUNPAUSE = "RUNSTART";
	public static String CONSUMERIMAGE = "consumer";
	public static String SETTASK = "http://1.anchang.sinaapp.com/DataServletTwo?action=settask";
	public static String GETFINISHTASK = "http://1.anchang.sinaapp.com/DataServletTwo?action=getfinishtask";
	public static String REGISTER = "http://1.anchang.sinaapp.com/Register?action=Register";
	public static String GUOHU = "http://1.anchang.sinaapp.com/DataServletTwo?action=guohu";
	public static String REGISTERXIAOSHOU = "http://1.anchang.sinaapp.com/DataServletTwo?action=registerxiaoshou";
	public static String GETCALLDETAIL = "http://1.anchang.sinaapp.com/DataServletTwo?action=getcalldetail";
	public static String GETXIAOSHOUMODEL = "http://1.anchang.sinaapp.com/DataServletTwo?action=getxiaoshoumodel";
	public static String GETTASK = "http://1.anchang.sinaapp.com/DataServletTwo?action=gettask";
	public static String GETNEWADD = "http://1.anchang.sinaapp.com/DataServletTwo?action=getnewadd";
	public static String ISLAOYEZHU = "http://1.anchang.sinaapp.com/DataServletTwo?action=islaoyezhu";
	public static String UNREGISTER = "http://1.anchang.sinaapp.com/DataServlet?action=unRegister";
	public static String INSERTCALL = "http://1.anchang.sinaapp.com/DataServlet?action=insertcall";
	public static String ISREGISTER = "http://1.anchang.sinaapp.com/Register?action=isRegister";
	public static String LOGIN = "http://1.anchang.sinaapp.com/LoginSevlet";
	public static String GETDAYDETAIL = "http://1.anchang.sinaapp.com/DataServlet?action=getdaydetail";
	public static String GETCALLRECORD = "http://1.anchang.sinaapp.com/DataServletTwo?action=getcallrecord";
	public static String GETONECONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=getoneconsumermodel";
	public static String GETCONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=getconsumermodel";
	public static String EXIT = "http://1.anchang.sinaapp.com/coreServlet"+"?action=exit&id=";
	public static String FASTUPDATACONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=fastupdataconsumermodel";
	public static String UPDATACONSUMERMODEL = "http://1.anchang.sinaapp.com/DataServlet?action=updataconsumermodel";
	static public PopupWindow settlementPop;
	public static void showLoad(Context context,View v){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View settlementView = mInflater.inflate(R.layout.loading_pop, null);
		if(settlementPop == null){
			
			LinearLayout loadView = (LinearLayout) settlementView.findViewById(R.id.loadView);
			TextView text = (TextView) settlementView.findViewById(R.id.text);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			            ViewGroup.LayoutParams.WRAP_CONTENT,
			            ViewGroup.LayoutParams.WRAP_CONTENT);
			 lp.gravity = Gravity.CENTER_VERTICAL;
			 ProgressBar progressBar = new ProgressBar(context);
			 progressBar.setLayoutParams(lp);
			 progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.loading));
			  loadView.addView(progressBar);
			  loadView.setVisibility(View.VISIBLE);
			  settlementPop = new PopupWindow(settlementView,  ViewGroup.LayoutParams.FILL_PARENT,
					  ViewGroup.LayoutParams.FILL_PARENT);
			  
			settlementPop.setFocusable(true);
		}
			int[] location = new int[2];
			v.getLocationOnScreen(location);
//			settlementPop.setAnimationStyle(R.style.PopupAnimation);
			
//			while(v.getVisibility() != View.VISIBLE){
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			
			settlementPop.showAtLocation(v, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
	}
	public static void dimissLoad(){
		if(settlementPop != null)
			settlementPop.dismiss();
	}
}
