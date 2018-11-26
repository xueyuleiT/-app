package com.example.view;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.amanager.R;
import com.example.util.Constant;
import com.example.util.NetworkData;

public class ExitPopwindow {

	public  PopupWindow exitPop;
	private Context client;
	private Button btnExit,btnCancel;
	private LinearLayout onExit,exit;
	private Handler handler;
	public ExitPopwindow(Context context){
		client = context;
		initUI();
	}
	
	private void initUI() {
		
		
		View v = ((Activity)client).getLayoutInflater().inflate(R.layout.exitpopwindow, null);
		btnExit = (Button) v.findViewById(R.id.btn_exit);
		btnCancel = (Button) v.findViewById(R.id.btn_cancel);
		onExit = (LinearLayout) v.findViewById(R.id.onExit);
		exit = (LinearLayout) v.findViewById(R.id.exit);
		exitPop = new PopupWindow(v,Constant.scWidth,Constant.scHeight);
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == 1){
					exit.setVisibility(View.GONE);
					onExit.setVisibility(View.VISIBLE);
				}else if(msg.what == 2){
					exitPop.dismiss();
				}
			}
			
		};
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
						
						System.out.println("userid = "+Constant.USERID);
						
						try {
							NetworkData.posturl(Constant.EXIT+Constant.USERID);
							Message message = new Message();
							message.what = 2;
							handler.sendMessage(message);
							((Activity)client).finish();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
							((Activity)client).finish();
						} catch (IOException e) {
							e.printStackTrace();
							((Activity)client).finish();
						}
						
					}
				}).start();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitPop.dismiss();
			}
		});
	}
	public void dismissPopwindow(){
		exitPop.dismiss();
	}
	public void showPopWindow(){
		exitPop.showAtLocation(btnExit, Gravity.CENTER, 0, 0);
	}
	
}
