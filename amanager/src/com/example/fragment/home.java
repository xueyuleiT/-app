package com.example.fragment;



import java.util.ArrayList;
import java.util.Collection;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.amanager.R;
import com.example.model.ConsumerModel;
import com.example.model.XiaoshouModel;
import com.example.util.Constant;
import com.example.util.NetworkData;
import com.example.view.ExitPopwindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class home extends FragmentActivity implements OnClickListener{
	public  Fragment[] mFragments;
	private RadioButton imgOne,imgTwo,imgThree;
	private int showFragment = 0;
	private ExitPopwindow exitPop;
	private FragmentManager fragmentManager;
	private static ProgressDialog pDia = null;
	static Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 6) {
				if (pDia != null) {
					pDia.dismiss();
					pDia = null;
				}

				Constant.dimissLoad();
			}
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		
		
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		Constant.scWidth = mDisplayMetrics.widthPixels;
		Constant.scHeight = mDisplayMetrics.heightPixels;
		
		initCompoments();
		initLinstener();
		setFragmentIndicator(0);
		
		pDia = new ProgressDialog(home.this);
		pDia.setMessage("正在努力加载。。。");
		pDia.setCancelable(false);
		pDia.setCanceledOnTouchOutside(false);
		pDia.show();
		
		initData(getApplicationContext(),imgOne);
		
	}

	public static void initData(final Context context,View v) {
		if(pDia == null)
			Constant.showLoad(context, v);
		new Thread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String arr = NetworkData.getMyModel(Constant.GETCONSUMERMODEL+"&quyu="+Constant.QUYU+"&phone=主管");
				if (arr != null && !arr.equals("")) {
					Gson gson = new Gson();
					Constant.conLst.clear();
					Constant.conLst.addAll((Collection<? extends ConsumerModel>) gson
							.fromJson(arr,new TypeToken<ArrayList<ConsumerModel>>() {}.getType()));
				}
				
				String strArr = NetworkData.getMyModel(Constant.GETXIAOSHOUMODEL+"&id="+Constant.USERPHONE);
				System.out.println("strarr = "+strArr);
				if (strArr != null && !strArr.equals("")) {
					Gson gson = new Gson();
					Constant.xiaoshouLst.clear();
					Constant.xiaoshouLst.addAll((Collection<? extends XiaoshouModel>) gson
							.fromJson(strArr,new TypeToken<ArrayList<XiaoshouModel>>() {}.getType()));
				}
				Constant.isLoading = true;
				context.sendBroadcast(new Intent("update"));
				myHandler.sendEmptyMessage(6);
			}
		}).start();
	}

	private void initLinstener() {
		imgTwo.setOnClickListener(this);
		imgOne.setOnClickListener(this);
		imgThree.setOnClickListener(this);
	}

	private void initCompoments() {
		imgTwo = (RadioButton) findViewById(R.id.imgTwo);
		imgOne = (RadioButton) findViewById(R.id.imgOne);
		imgThree = (RadioButton) findViewById(R.id.imgThree);
		exitPop = new ExitPopwindow(this);
	}
	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[3];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(
				R.id.OneFragment);
		mFragments[1] = fragmentManager.findFragmentById(
				R.id.TwoFragment);
		mFragments[2] = fragmentManager.findFragmentById(
				R.id.ThreeFragment);
		fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[2])
				.hide(mFragments[1]).show(mFragments[whichIsDefault]).commit();
		imgOne.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imgTwo:
			if(showFragment != 1){
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[0]).hide(mFragments[2]).show(mFragments[1]).commit();
				showFragment = 1;
				}
			break;
		case R.id.imgOne:
			if(showFragment != 0){
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[1]).hide(mFragments[2]).show(mFragments[0]).commit();
				showFragment = 0;
			}
			break;
		case R.id.imgThree:
			if(showFragment != 2){
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[1]).hide(mFragments[0]).show(mFragments[2]).commit();
				showFragment = 2;
			}
			break;
		}
	}
	@Override
	public void onStop() {
		Constant.RUNPAUSE = "RUNPAUSE";
		super.onStop();
	}

	@Override
	protected void onStart() {
		Constant.RUNPAUSE = "RUNSTART";
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		if(exitPop.exitPop.isShowing()){
			exitPop.dismissPopwindow();
		}else
			exitPop.showPopWindow();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(showFragment == 0){
			fragmentManager.beginTransaction().hide(mFragments[0])
			.hide(mFragments[1]).show(mFragments[showFragment]).commit();
			imgOne.setChecked(true);
			imgTwo.setChecked(false);
		}else{
			fragmentManager.beginTransaction().hide(mFragments[0])
			.hide(mFragments[1]).show(mFragments[showFragment]).commit();
			imgTwo.setChecked(true);
			imgOne.setChecked(false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
	}


	@Override
	protected void onDestroy() {
			
		super.onDestroy();
	}


}
