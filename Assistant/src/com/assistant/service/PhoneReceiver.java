package com.assistant.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import org.apache.http.client.ClientProtocolException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.fragment.OneFragment;
import com.assistant.model.ConsumerModel;
import com.assistant.model.TextMessage;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.NetworkData;
import com.assistant.view.HandDown;
import com.assistant.view.HandDownSave;
import com.example.assistant.R;
import com.google.gson.Gson;
 
public class PhoneReceiver extends BroadcastReceiver {
 
	private Context context = null;
	static TelephonyManager tm;
	static String phoneNumber = "";
	static public PopupWindow settlementPop;
	static boolean isAccept = false;
	static ConsumerModel con;
	static String phoneState = "";
	static Thread t ;
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				Toast.makeText(context, "请检查网络", 0).show();
			}else if(msg.what == 2){
//				TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
//				tm.listen(listener1, PhoneStateListener.LISTEN_CALL_STATE);
			}else if(msg.what == 3){
				Toast.makeText(context, "该客户不属于您", 0).show();
			}
		}
		
	};
	
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
			
			settlementPop.showAtLocation(v, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
	}
	public static void dimissLoad(){
		if(settlementPop != null)
			settlementPop.dismiss();
	}
	@Override
	public void onReceive(final Context context, final Intent intent) {
		
		if(this.context == null)
			this.context = context;
		
		if(tm == null){
			if(Constant.USERPHONE.equals("")){
				SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
				Constant.USERPHONE = sp.getString("USER_PHONE", "");
				Constant.USERNAME = sp.getString("USER_NAME", "");
			}
			tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
		if(Constant.scWidth == 0 || Constant.scHeight == 0){
			
        	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(mDisplayMetrics);
            
	    	Constant.scWidth = mDisplayMetrics.widthPixels;
	    	Constant.scHeight = mDisplayMetrics.heightPixels;
        }
		// 如果是去电
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			System.out.println("num = "+phoneNumber);
			System.out.println("去电");
			System.out.println("start = "+CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
			 t =  new Thread(new Runnable() {
				
				@Override
				public void run() {
					Gson gson = new Gson();
					String result = NetworkData.getMyModel(Constant.GETONECONSUMERMODEL+"&phone="+phoneNumber);
					System.out.println("result = "+result);
					if(result != null && !result.equals("") && !result.equals("error")){
						con = gson.fromJson(result, ConsumerModel.class);
						if(!con.getConsultantphone().equals(Constant.USERPHONE))
							myHandler.sendEmptyMessage(3);
							
//						Intent it = new Intent(context,FxService.class);
//						Bundle bd = new Bundle();
//						bd.putSerializable("consumermodel", con);
//						it.putExtras(bd);
//						context.startService(it);
					int i = 0;
							while (i < 50) {
								try {
									Thread.sleep(200);
									i++;
									if (phoneState.length() >4) {
										Intent it = new Intent(context,FxService.class);
										Bundle bd = new Bundle();
										bd.putSerializable("consumermodel", con);
										it.putExtras(bd);
										context.startService(it);
										i = 0;
										break;
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
					}
				}
			});
			 t.start();
			  
			
		}
	}
	 private Long upDateRecordTime(String number) {  
		 long date = 0;
		 if(number != null && !number.equals("")  && context != null){
			 try {
				Thread.sleep(500);
				 ContentResolver resolver = context.getContentResolver(); 
				 String dat = "";
				 Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.DURATION,CallLog.Calls.DATE}, "number=?",  new String[]{number},  "_id desc limit 1");  
				 if(cursor.moveToFirst()) { 
					 date = cursor.getLong(0);
					 dat = cursor.getString(1);
				 }
				 if(cursor != null){
					 cursor.close();
				 }
//				 System.out.println("date = "+date +"   差值  = "+(System.currentTimeMillis()- Long.parseLong(dat))/1000);
				 if(date > 0 && (System.currentTimeMillis()- Long.parseLong(dat))/1000  < 50+date){
					 
				 }else
					 date = 0;
				 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 }
		return date;
	 }  
	 
	 
	 private void intentHandDown(){
		 Intent it = new Intent(context,HandDown.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			Bundle bd = new Bundle();
			bd.putSerializable("consumer", con);
			it.putExtras(bd);
			context.startActivity(it);
	 }
	 
	 private void intentHandDownSave(final String number,final IntentInterface listener){
		 if(t != null && t.isAlive())
			 t.interrupt();
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				Gson gson = new Gson();
				String result = NetworkData.getMyModel(Constant.GETONECONSUMERMODEL+"&phone="+number);
				System.out.println("result = "+result);
				if(result != null && !result.equals("error") && !result.equals("")){
					con = gson.fromJson(result, ConsumerModel.class);
					if(!con.getConsultantphone().equals(Constant.USERPHONE))
						myHandler.sendEmptyMessage(3);
					else
						intentHandDown(); 
					con = null;
				}else if(result != null && result.equals("error")){
					myHandler.sendEmptyMessage(1);
				}else if(result == null || (result != null && result.equals("")))
					listener.intentTo(number);
			}
		}).start();
	 }
	 
	PhoneStateListener listener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				phoneState+="idle";
				final Long stopTime = System.currentTimeMillis();
				final SimpleDateFormat dateForamt= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				System.out.println("CALL_STATE_IDLE");
				
				System.out.println("phoneState = "+phoneState);
				System.out.println("phoneNum = "+phoneNumber);
				if (phoneState.equals("offhookidle")) {// 打出
					context.sendBroadcast(new Intent("dismiss"));
					if(phoneNumber.length() == 11){
						final long callTime = upDateRecordTime(phoneNumber);
						
						if(callTime == 0 && con == null){
							String number = new String();
							number = phoneNumber;
							intentHandDownSave(number,new IntentInterface() {
								
								@Override
								public void intentTo(String number) {
									 	Intent it = new Intent(context,HandDownSave.class);
										it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
										Bundle bd = new Bundle();
										bd.putString("phone", number);
										bd.putString("type", "call");
										it.putExtras(bd);
										context.startActivity(it);
								}
							});
						}
						
						if(callTime == 0 || con == null){
							if(callTime != 0){
								Intent it = new Intent(context,HandDownSave.class);
								it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
								Bundle bd = new Bundle();
								bd.putString("phone", phoneNumber);
								bd.putString("type", "callSave");
//								bd.putSerializable("consumer", con);
								bd.putString("flag","false");
								bd.putLong("callTime",callTime);
								bd.putLong("stopTime",stopTime);
								it.putExtras(bd);
								context.startActivity(it);
							}else if(con != null && con.getConsultantphone().equals(Constant.USERPHONE)){
								
								intentHandDown(); 
//								context.sendBroadcast(new Intent("updatecall"));
							}
							phoneNumber = "";
							con = null;
						}else{
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									try {
										
										if(con.getConsultantphone().equals(Constant.USERPHONE)){
											intentHandDown();
										TextMessage t = NetworkData.posturl(Constant.INSERTCALL+"&start="+URLEncoder.encode(dateForamt.format(stopTime - callTime*1000), "UTF-8")+"&stop="+URLEncoder.encode(dateForamt.format(stopTime), "UTF-8")+"&isCallIn=false&phone="+phoneNumber+"&myphone="+Constant.USERPHONE);
										if(t.getContent().equals("更新成功")){
											Constant.isNeedRefresh = true;
											int size = OneFragment.taskArr.size();
											for (int i = 0; i < size; i++) {
												if(OneFragment.taskArr.get(i).getCustomer_phone().equals(phoneNumber)){
													OneFragment.taskArr.remove(i);
													context.sendBroadcast(new Intent("updatetask"));
													break;
												}
											}
										}
										context.sendBroadcast(new Intent("updatecall"));
										}
										phoneNumber = "";
										con = null;
									} catch (ClientProtocolException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}).start();
						}
					}
				}else if(phoneState.equals("ringingidle")){//打进不接
					context.sendBroadcast(new Intent("dismiss"));
					if(incomingNumber!= null && incomingNumber.length() == 11){
						
						if(con == null){
							String number = new String();
							number = incomingNumber;
							intentHandDownSave(number,new IntentInterface() {
								
								@Override
								public void intentTo(String number) {
									 	Intent it = new Intent(context,HandDownSave.class);
										it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
										Bundle bd = new Bundle();
										bd.putString("phone", number);
										bd.putString("type", "call");
										it.putExtras(bd);
										context.startActivity(it);
								}
							});
						}
						
							if(con != null && con.getConsultantphone().equals(Constant.USERPHONE)){
								intentHandDown(); 
							}
					}
					phoneNumber = "";
					con = null;
				}else if(phoneState.equals("ringingoffhookidle")){//打进接
					context.sendBroadcast(new Intent("dismiss"));
					if(phoneNumber!= null && phoneNumber.length() == 11){
						final long callTime = upDateRecordTime(phoneNumber);
						if(callTime == 0 || con == null){
							if(callTime != 0){
								Intent it = new Intent(context,HandDownSave.class);
								it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
								Bundle bd = new Bundle();
								bd.putString("phone", phoneNumber);
								bd.putString("type", "callSave");
//								bd.putSerializable("consumer", con);
								bd.putString("flag","true");
								bd.putLong("callTime",callTime);
								bd.putLong("stopTime",stopTime);
								it.putExtras(bd);
								context.startActivity(it);
							}else if(con != null && con.getConsultantphone().equals(Constant.USERPHONE)){
								
								intentHandDown(); 
								context.sendBroadcast(new Intent("updatecall"));
							}
							phoneNumber = "";
							con = null;
						}else{
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									try {
										if(con.getConsultantphone().equals(Constant.USERPHONE)){
											intentHandDown();
											TextMessage t = NetworkData.posturl(Constant.INSERTCALL+"&start="+URLEncoder.encode(dateForamt.format(stopTime - callTime*1000), "UTF-8")+"&stop="+URLEncoder.encode(dateForamt.format(stopTime), "UTF-8")+"&isCallIn=true&phone="+phoneNumber+"&myphone="+Constant.USERPHONE);
											if(t.getContent().equals("更新成功")){
												Constant.isNeedRefresh = true;
												int size = OneFragment.taskArr.size();
												for (int i = 0; i < size; i++) {
													if(OneFragment.taskArr.get(i).getCustomer_phone().equals(incomingNumber)){
														OneFragment.taskArr.remove(i);
														context.sendBroadcast(new Intent("updatetask"));
														break;
													}
												}
											}
											context.sendBroadcast(new Intent("updatecall"));
										}
										con = null;
										phoneNumber = "";
									} catch (ClientProtocolException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}).start();
						}
					}
				}
				
				phoneState = "";
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				phoneState+="offhook";
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				phoneState+="ringing";
				System.out.println("CALL_STATE_RINGING");
				System.out.println("ringing = "+phoneNumber);
				if(incomingNumber!= null && incomingNumber.length() > 0){
					phoneNumber = incomingNumber;
					  t = new Thread(new Runnable() {
							
							@Override
							public void run() {
								Gson gson = new Gson();
								String result = NetworkData.getMyModel(Constant.GETONECONSUMERMODEL+"&phone="+incomingNumber);
								System.out.println("arr = "+result);
								if(result != null && !result.equals("") && !result.equals("error")){
									con = gson.fromJson(result, ConsumerModel.class);
									if(con != null && !con.getConsultantphone().equals(Constant.USERPHONE))
										myHandler.sendEmptyMessage(3);

									Intent it = new Intent(context,FxService.class);
									Bundle bd = new Bundle();
									bd.putSerializable("consumermodel", con);
									it.putExtras(bd);
									context.startService(it);
								}else{
									myHandler.sendEmptyMessage(1);
								}
							}
						});
					  t.start();
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	};
	
	
}