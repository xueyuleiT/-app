package com.assistant.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RadioButton;
import android.widget.Toast;

import com.assistant.model.ConsumerModel;
import com.assistant.model.TextMessage;
import com.assistant.service.PhoneReceiver;
import com.assistant.utils.Constant;
import com.assistant.utils.NetworkData;
import com.assistant.utils.Sort;
import com.example.assistant.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Home extends  FragmentActivity implements OnClickListener{
	public  Fragment[] mFragments;
	private RadioButton imgOne,imgTwo,imgThree,imgFour,imgFive;
	private int showFragment = 0;
//	private GestureDetector detector;
	private FragmentManager  fragmentManager;
    public static ProgressDialog pDia = null;
    private ExitPopwindow exitPop;
    private static Sort mSort;
//    private ActivityManager mActivityManager;
    private static boolean isVisible = false;
	 Animation scaleAnimation = new ScaleAnimation(0.5f, 1.0f,0.5f,1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f,
	            Animation.RELATIVE_TO_PARENT, 0.5f);  
	 private Handler myHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == 1){
					Toast.makeText(getApplicationContext(), "该用户已被"+((String)msg.obj)+"注册", 0).show();
				}else if(msg.what == 2){
					PhoneReceiver.showLoad(getApplicationContext(), imgOne);
				}else if(msg.what == 3){
					PhoneReceiver.dimissLoad();
				}
			}
			
		};
		private static Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == 2){
					if (pDia != null) {
						pDia.cancel();
						pDia = null;
					}
					PhoneReceiver.dimissLoad();
				}
			}
			
		};
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home);
		
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		Constant.scHeight = mDisplayMetrics.widthPixels;
		Constant.scWidth = mDisplayMetrics.heightPixels;
		
		
		mSort = new Sort();
		int temp = Constant.scHeight;
		Constant.scHeight = Constant.scWidth;
		Constant.scWidth = temp;
		
		pDia = new ProgressDialog(Home.this);
		pDia.setMessage("正在努力加载。。。");
		pDia.setCancelable(false);
		pDia.setCanceledOnTouchOutside(false);
		pDia.show();
		
		handleIntent(getIntent());  
//		detector = new GestureDetector(this);

		initCompoments();
		initLinstener();
		setFragmentIndicator(0);
//		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//			String	mPackageName = getPackageName();
//			List<RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
//				while (!mPackageName.equals(tasksInfo.get(0).topActivity
//						.getPackageName())) {
//						try {
//							Thread.sleep(200);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//				}
//				myHandler.sendEmptyMessage(4);
//				isVisible = true;
//				while(!Constant.isLoading){
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				myHandler.sendEmptyMessage(3);
//			}
//		}).start();
		
		getDate(getApplicationContext(),imgTwo);
	}
	
	
	



	private void initLinstener() {
		imgOne.setOnClickListener(this);
		imgTwo.setOnClickListener(this);
		imgThree.setOnClickListener(this);
		imgFour.setOnClickListener(this);
		imgFive.setOnClickListener(this);
		
//		imgOne.setOnTouchListener(this);
//		imgTwo.setOnTouchListener(this);
//		imgThree.setOnTouchListener(this);
//		imgFour.setOnTouchListener(this);
//		imgFive.setOnTouchListener(this);
		
	}
	private void initCompoments() {
		imgTwo = (RadioButton) findViewById(R.id.imgTwo);
		imgOne = (RadioButton) findViewById(R.id.imgOne);		
		imgThree = (RadioButton) findViewById(R.id.imgThree);
		imgFour = (RadioButton) findViewById(R.id.imgFour);
		imgFive = (RadioButton) findViewById(R.id.imgFive);
		exitPop = new ExitPopwindow(this);
		imgOne.setChecked(true);
		showFragment = 0;
	}

	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[5];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(
				R.id.OneFragment);
		mFragments[1] = fragmentManager.findFragmentById(
				R.id.TwoFragment);
		mFragments[2] = fragmentManager.findFragmentById(
				R.id.ThreeFragment);
		mFragments[3] = fragmentManager.findFragmentById(
				R.id.FourFragment);
		mFragments[4] = fragmentManager.findFragmentById(
				R.id.FiveFragment);
		fragmentManager.beginTransaction().hide(mFragments[0])
				.hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]).show(mFragments[whichIsDefault]).commit();
	
	}
	
	private void show(final int num){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				fragmentManager.beginTransaction()
				.hide(mFragments[0]).hide(mFragments[2])
				.hide(mFragments[3]).hide(mFragments[4]).hide(mFragments[1]).show(mFragments[num]).commit();
				showFragment = num;
			}
		}).start();
	}
	
	
	public static void getDate(final Context context,View v){
		if(isVisible)
			PhoneReceiver.showLoad(context,v);
		new Thread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				String arr = NetworkData.getMyModel(Constant.GETCONSUMERMODEL+"&quyu="+Constant.QUYU+"&phone="+Constant.USERPHONE);
				Constant.conLst.clear();
				System.out.println("arr = "+arr);
				if(arr != null && !arr.equals("") && !arr.equals("error")){
				Gson gson = new Gson();
				Constant.conLst.addAll((Collection<? extends ConsumerModel>) gson.fromJson(arr,
						new TypeToken<ArrayList<ConsumerModel>>() {}.getType()));
				}
				if(mSort == null)
					mSort = new Sort();
					
				if(Constant.conLst.size() > 0 ){
					int length = Constant.conLst.size();
					String[] arrs = new String[length];
					for (int i = 0; i < length; i++) {
						ConsumerModel con = Constant.conLst.get(i);
						con.setPinyin(mSort.getAllPinYinHeadChar(con.getCustomer_name()));
						
						if(con.getZijinshili().equals("")){
						}else if(con.getZijinshili().equals("50万-100万")){
							con.setCallGrade(con.getCallGrade() + 2);
						}else if(con.getZijinshili().equals("100万-150万")){
							con.setCallGrade(con.getCallGrade() + 3);
						}else if(con.getZijinshili().equals("150万-200万")){
							con.setCallGrade(con.getCallGrade() + 4);
						}else{
							con.setCallGrade(con.getCallGrade() + 5);
						}
						
						if(con.getKehushuxing().equals("老带新")){
							con.setCallGrade(con.getCallGrade() + 10);
						}else if(con.getKehushuxing().equals("老业主")){
							con.setCallGrade(con.getCallGrade() + 4);
						}else{
							con.setCallGrade(con.getCallGrade() + 7);
						}
						
						if(con.getYixiang().equals("强")){
							con.setCallGrade(con.getCallGrade() + 10);
						}else if(con.getYixiang().equals("中")){
							con.setCallGrade(con.getCallGrade() + 6);
						}else{
							con.setCallGrade(con.getCallGrade() + 3);
						}
						
						if(con.getKehuzu().equals("重要客户")){
							con.setCallGrade(con.getCallGrade() + 10);
						}else if(con.getKehuzu().equals("一般客户")){
							con.setCallGrade(con.getCallGrade() + 5);
						}else
							con.setCallGrade(0);
						
						arrs[i] = Constant.conLst.get(i).getCustomer_phone()+"---"+Constant.conLst.get(i).getCustomer_name();
					}
					
					Constant.conLst = mSort.autoSort(Constant.conLst);
					Constant.tellPhones = new String[][]{arrs,arrs,arrs,arrs,arrs};
					Constant.isNeedRefresh = true;
					Intent it = new Intent("update");
					context.sendBroadcast(it);
				}else{
					if(Constant.conLst.size() == 0){
						String[] arrs = new String[3];
						for (int i = 0; i < 3; i++) {
							arrs[i] = "空";
						}
						Constant.tellPhones = new String[][]{arrs,arrs,arrs,arrs,arrs};
						Intent it = new Intent("update");
						context.sendBroadcast(it);
						
					}
				}
				handler.sendEmptyMessage(2);
				Constant.isLoading = true;
			}
		}).start();

	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imgTwo:
			if(showFragment != 1){
				imgTwo.startAnimation(scaleAnimation);
 				scaleAnimation.setDuration(500); 
 				show(1);
			}
			break;
		case R.id.imgThree:
			if(showFragment != 2){
				imgThree.startAnimation(scaleAnimation);
 				scaleAnimation.setDuration(500); 
 				show(2);
			}
			break;
		case R.id.imgFour:
			if(showFragment != 3){
				imgFour.startAnimation(scaleAnimation);
 				scaleAnimation.setDuration(500); 
 				show(3);
			}
			break;
		case R.id.imgFive:
			if(showFragment != 4){
				imgFive.startAnimation(scaleAnimation);
 				scaleAnimation.setDuration(500); 
 				show(4);
			}
			break;
		case R.id.imgOne:
			if(showFragment != 0){
				imgOne.startAnimation(scaleAnimation);
 				scaleAnimation.setDuration(500); 
 				show(0);
			}
			break;
		}
	}
	@Override  
	protected void onNewIntent(Intent intent) {  
	    setIntent(intent);  
	    handleIntent(intent);  
	}
	
	public boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	private void handleIntent(Intent intent) {  
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {  
	      final String query = intent.getStringExtra(SearchManager.QUERY);  
	      if(query.length() == 11 && isNumeric(query)){
	    	  new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
			    		  TextMessage tMsg = NetworkData.posturl(Constant.ISREGISTER+"&phone="+query);
						if(tMsg.getContent().equals("未注册")){
							  Intent it = new Intent(getApplicationContext(),Register.class);
							  it.putExtra("type", "save");
							  it.putExtra("phone", "query");
						      startActivity(it);
						}else{
							Message msg = new Message();
							msg.what = 1;
							msg.obj = tMsg.getContent();
							myHandler.sendMessage(msg);
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
	    	  
	      }else{
	    	  Toast.makeText(getApplicationContext(), "请输入11位电话", 0).show();
	      }
	    }  
	}  
	@Override  
	public boolean onSearchRequested() {  
	        startSearch(null, false, null, false);
			return true;
	}
//	@Override
//	public boolean onDown(MotionEvent e) {
//		return false;
//	}
//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
//		System.out.println("touch");
//		if (e1.getY() - e2.getY() < -120) {
//			System.out.println("touch = "+ (e1.getY() - e2.getY())); 
//			onSearchRequested();
//			return true;
//		} 
//		return false;
//	}
//	@Override
//	public void onLongPress(MotionEvent e) {
//		
//	}
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
//		return false;
//	}
//	@Override
//	public void onShowPress(MotionEvent e) {
//		
//	}
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//		return false;
//	}  
	
	
	@Override
	public void onBackPressed() {
		if(exitPop.exitPop.isShowing()){
			exitPop.dismissPopwindow();
		}else
			exitPop.showPopWindow();
	}



	@Override
	protected void onResume() {
		System.out.println("onpause");
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
	}






	@Override
	protected void onDestroy() {
		Constant.conLst.clear();
		super.onDestroy();
	}
	
	
	
}
