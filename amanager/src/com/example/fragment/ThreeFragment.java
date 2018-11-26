package com.example.fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;

import com.example.adapter.GroupAdapter;
import com.example.adapter.MyListAdapter;
import com.example.adapter.XiaoshouAdapter;
import com.example.amanager.R;
import com.example.model.ConsumerModel;
import com.example.model.SelectModel;
import com.example.timeselect.JudgeDate;
import com.example.timeselect.ScreenInfo;
import com.example.timeselect.WheelMain;
import com.example.util.Constant;
import com.example.util.CurrentTime;
import com.example.view.PieChartView;
import com.example.view.PopMenu;
import com.example.view.myListView;


public class ThreeFragment extends Fragment{
	private LinearLayout rootView;
	private PieChartView mChartView;
//	private ImageView imgSelect;
	private AbstractWheel select;
	private TextView leibie,timeSelect,toYearMonth;
	private ArrayList<SelectModel> selectArr = new ArrayList<SelectModel>();
	private boolean scrolling = false,isDelete = false,isVisible = false;
	private int currentItem = 2;
	private myListView mList;
	private MyListAdapter mAdapter;
	private ProgressDialog pDia = null;
	private int i = 0;
	WheelMain wheelMain;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	ArrayList<ConsumerModel> sortArr = new ArrayList<ConsumerModel>();
	HashMap<String,ArrayList<ConsumerModel>> hashMap = new HashMap<String,ArrayList<ConsumerModel>>();
	ArrayList<double[]> values;
	int currenPosition = 2;
	private boolean isYear = false;
	private PopMenu popMenu;
	String Group[] = new String[]{
				"意向","业态","户型","面积","认知渠道","资金实力","客户属性","客户组"
				 ,"预算"};
	 private Handler myHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == 1){
					Toast.makeText(getActivity(), "该用户已被"+((String)msg.obj)+"注册", 0).show();
					Constant.dimissLoad();
				}else if(msg.what == 2){
					rootView.removeAllViews();
					mChartView = new PieChartView(getActivity(),(HashMap<String, ArrayList<ConsumerModel>>) msg.obj);
					rootView.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,
					          LayoutParams.FILL_PARENT));
					
					if(selectArr.size() == 0){
						SelectModel model = new SelectModel();
						model.setsName(Group[currentItem]);
						selectArr.add(model);
						mAdapter  = new MyListAdapter(getActivity(), selectArr);
						mList.setAdapter(mAdapter);
					}
					else if(!selectArr.get(selectArr.size() -1).isSelect()){
						boolean isSelect = false;
						for (int i = 0; i < selectArr.size(); i++) {
							if(selectArr.get(i).getsName().equals(Group[currentItem])){
								isSelect = true;
								Toast.makeText(getActivity(), "该条件已选择", 0).show();
								break;
							}
						}
						if(!isSelect){
							SelectModel model = new SelectModel();
							model.setsName(Group[currentItem]);
							selectArr.add(model);
							selectArr.get(selectArr.size() - 2).setSelect(true);
							mAdapter.notifyDataSetChanged();
						}
					}else if(selectArr.get(selectArr.size() -1).isSelect()){
						boolean isSelect = false;
						for (int i = 0; i < selectArr.size() - 1; i++) {
							if(i == selectArr.size() - 1){
								break;
							}
							else if(selectArr.get(i).getsName().equals(Group[currentItem])){
								isSelect = true;
								Toast.makeText(getActivity(), "该条件已选择", 0).show();
								break;
							}
						}
						if(!isSelect){
							SelectModel model = new SelectModel();
							model.setsName(Group[currentItem]);
							selectArr.set(selectArr.size() - 1, model);
							mAdapter.notifyDataSetChanged();
						}
					}
					Constant.dimissLoad();
				}else if(msg.what == 3){
					rootView.removeAllViews();
					mChartView = new PieChartView(getActivity(),(HashMap<String, ArrayList<ConsumerModel>>) msg.obj);
					rootView.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,
					          LayoutParams.FILL_PARENT));
					Constant.dimissLoad();
				}else if(msg.what == 4){
					Constant.dimissLoad();
				}else if(msg.what == 5){
					Constant.showLoad(getActivity(), leibie);
				}else if(msg.what == 6){
					Constant.showLoad(getActivity(), leibie);
					initSortArr();
					
					int size = selectArr.size();
					if(size > 0){
						for (int i = 0; i < 9; i++) {
							if(selectArr.get(0).getsName().equals(Group[i])){
								initHash(i); 
								break;
							}
						}
					}
					for (int i = 1; i < size; i++) {
						getAddHash(hashMap,selectArr.get(i).getsName());
					}
					rootView.removeAllViews();
					mChartView = new PieChartView(getActivity(),hashMap);
					rootView.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,
					          LayoutParams.FILL_PARENT));
					Constant.dimissLoad();
				}
				if(pDia != null){
					pDia.dismiss();
					pDia = null;
				}
					
			}
			
		};
		 public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					if(i == 0){
						pDia = new ProgressDialog(context);
						pDia.setMessage("正在努力加载。。。");
						pDia.setCancelable(false);
						pDia.setCanceledOnTouchOutside(false);
						pDia.show();
						i ++;
					}else
						Constant.showLoad(getActivity(), leibie);
					initSortArr();
					
					int size = selectArr.size();
					if(size > 0){
						for (int i = 0; i < 9; i++) {
							if(selectArr.get(0).getsName().equals(Group[i])){
								initHash(i); 
								break;
							}
						}
						for (int i = 1; i < size; i++) {
							if(hashMap.size() > 0)
								hashMap = getAddHash(hashMap,selectArr.get(i).getsName());
						}
						Message msg = new Message();
						msg.what = 3;
						msg.obj = hashMap;
						myHandler.sendMessage(msg);
					}else{
						initData();
					}
					
					
					
//					if(Constant.conLst.size() > 0){
//						if(i == 0){
//							pDia = new ProgressDialog(context);
//							pDia.setMessage("正在努力加载。。。");
//							pDia.setCancelable(false);
//							pDia.setCanceledOnTouchOutside(false);
//							pDia.show();
//							i ++;
//						}else
//							Constant.showLoad(getActivity(), leibie);
//						initSortArr();
//						
//						int size = selectArr.size();
//						if(size > 0){
//							for (int i = 0; i < 9; i++) {
//								if(selectArr.get(0).getsName().equals(Group[i])){
//									initHash(i); 
//									break;
//								}
//							}
//						}
//						for (int i = 1; i < size; i++) {
//							getAddHash(hashMap,selectArr.get(i).getsName());
//						}
//						Message msg = new Message();
//						msg.what = 3;
//						msg.obj = hashMap;
//						myHandler.sendMessage(msg); 
//					}
					
			}
		};
		
		 private void register_BroadCast() {
				IntentFilter filter = new IntentFilter();
				filter.addAction("update");
				filter.setPriority(Integer.MAX_VALUE);
				getActivity().registerReceiver(m_BroadcastReceiver, filter);
			}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		register_BroadCast();
		
//		final ActivityManager mActivityManager = ((ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE));
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//			String	mPackageName = getActivity().getPackageName();
//			List<RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
//				while (!mPackageName.equals(tasksInfo.get(0).topActivity
//						.getPackageName())) {
//						try {
//							Thread.sleep(200);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//				}
//				myHandler.sendEmptyMessage(5);
//				isVisible = true;
//				while(!Constant.isLoading){
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				myHandler.sendEmptyMessage(4);
//			}
//		}).start();
		
		initLstener();
	}
	
	private void initLstener() {
//		imgSelect.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!selectYear){
//					str = "yyyy";
//					dateFormat = new SimpleDateFormat(str);
//					timeSelect.setText(CurrentTime.getCurrentTime(str));
//					imgSelect.setBackgroundResource(R.drawable.month);
//				}
//				else{
//					str = "yyyy-MM";
//					dateFormat = new SimpleDateFormat(str);
//					timeSelect.setText(CurrentTime.getCurrentTime(str));
//					imgSelect.setBackgroundResource(R.drawable.year);
//				}
//				selectYear = !selectYear;
//			}
//		});
		toYearMonth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimeSelect(toYearMonth);
			}
		});
		timeSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTimeSelect(timeSelect);
			}
		});
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				if(!selectArr.get(position).isSelect()){
					selectArr.get(position).setSelect(true);
					v.findViewById(R.id.imgDelete).startAnimation(getRotateAnimation(0, 45, 300));
				}else{
					selectArr.remove(position);
					if(selectArr.size() > 1){
						selectArr.get(selectArr.size() - 1).setSelect(false);
					}else if(selectArr.size() == 1){
						selectArr.get(selectArr.size() - 1).setSelect(false);
					}
					
					mAdapter.notifyDataSetChanged();
					
//					initSortArr();
					Constant.showLoad(getActivity(), leibie);
					int size = selectArr.size();
					if(size > 0){
						for (int i = 0; i < 9; i++) {
							if(selectArr.get(0).getsName().equals(Group[i])){
								initHash(i); 
								break;
							}
						}
						for (int i = 1; i < size; i++) {
							if(hashMap.size() > 0)
								hashMap = getAddHash(hashMap,selectArr.get(i).getsName());
						}
						Message msg = new Message();
						msg.what = 3;
						msg.obj = hashMap;
						myHandler.sendMessage(msg);
					}else{
						initData();
					}
					
					Constant.dimissLoad();
				}
			}
		});
		
//		firstView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!isDelete){
//					firstView.startAnimation(getRotateAnimation(0, 45, 300));
//				}else{
//					firstView.startAnimation(getRotateAnimation(45, 0, 300));
//				}
//				isDelete = !isDelete;
//			}
//		});
		leibie.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popMenu.showAsDropDown(v);
			}
		});
		select.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(AbstractWheel wheel) {
				 scrolling = true;
			}
			
			@Override
			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				switch(select.getCurrentItem()){
				case 0:
					currentItem = 0;
					break;
				case 1:
					currentItem = 1;
					break;
				case 2:
					currentItem = 2;
				break;
				case 3:
					currentItem = 3;
					break;
				case 4:
					currentItem = 4;
					break;
				case 5:
					currentItem = 5;
					break;
				case 6:
					currentItem = 6;
					break;
				case 7:
					currentItem = 7;
					break;
				case 8:
					currentItem = 8;
					break;
				}
				
				if(selectArr.size() == 0){
					initData();
				}
				else if(!selectArr.get(selectArr.size() -1).isSelect()){
					boolean isSelect = false;
					for (int i = 0; i < selectArr.size(); i++) {
						if(selectArr.get(i).getsName().equals(Group[currentItem])){
							isSelect = true;
							Toast.makeText(getActivity(), "该条件已选择", 0).show();
							break;
						}
					}
					if(!isSelect){
						initData();
					}
				}else if(selectArr.get(selectArr.size() -1).isSelect()){
					boolean isSelect = false;
					for (int i = 0; i < selectArr.size() - 1; i++) {
						if(selectArr.get(i).getsName().equals(Group[currentItem])){
							isSelect = true;
							Toast.makeText(getActivity(), "该条件已选择", 0).show();
							break;
						}
					}
					if(!isSelect){
						initData();
					}
				}
				
			}
		});
	}
	private void initUI() {
		rootView = (LinearLayout) getActivity().findViewById(R.id.rootView);
		select = (AbstractWheel) getActivity().findViewById(R.id.select);
		leibie = (TextView) getActivity().findViewById(R.id.leibie);
		mList = (myListView) getActivity().findViewById(R.id.selectList);
		timeSelect = (TextView) getActivity().findViewById(R.id.fromYearMonth);
		toYearMonth = (TextView) getActivity().findViewById(R.id.toYearMonth);
		leibie.setText("客户比例");
		
		
		popMenu = new PopMenu(getActivity(), getResources().getDimensionPixelSize(
				R.dimen.popmenu_x));
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		popMenu.addItems(new String[] { "已成交","未成交","客户比例"});
		timeSelect.setText(CurrentTime.getBeforeMonthTime("yyyy-MM-dd"));
		toYearMonth.setText(CurrentTime.getCurrentTime("yyyy-MM-dd"));
		
		select.setCyclic(false);
		select.setVisibleItems(5);
		select.setViewAdapter(new GroupAdapter(getActivity(),Group));
		select.setCurrentItem(currentItem);
		
	}
	
	private void initSortArr() {
		sortArr.clear();
		while(!Constant.isLoading){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int size = Constant.conLst.size();
		for (int i = 0; i < size; i++) {
			if(comparesTime(Constant.conLst.get(i).getDatatime().substring(0, 10), timeSelect.getText().toString())
					&& comparesTime(toYearMonth.getText().toString(),Constant.conLst.get(i).getDatatime().substring(0, 10))){
				if(leibie.getText().toString().equals("已成交")){
					if(!Constant.conLst.get(i).getKehushuxing().equals("新客户") && !Constant.conLst.get(i).getChengjiaojine().equals("")){
						sortArr.add(Constant.conLst.get(i));
					}
				}else if(leibie.getText().toString().equals("未成交")){
					if(Constant.conLst.get(i).getKehushuxing().equals("新客户")){
						sortArr.add(Constant.conLst.get(i));
					}
				}else if(leibie.getText().toString().equals("客户比例")){
					sortArr.add(Constant.conLst.get(i));
				}
			}
		}
		
	}

	private void showTimeSelect(final TextView tv){
		LayoutInflater inflater=LayoutInflater.from(getActivity());
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		
		ScreenInfo screenInfo = new ScreenInfo(getActivity());
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = timeSelect.getText().toString();
		Calendar calendar = Calendar.getInstance();
		if(JudgeDate.isDate(time, "yyyy-MM-dd")){
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year,month,day);
		new AlertDialog.Builder(getActivity())
		.setTitle("选择日期")
		.setView(timepickerview)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					if(comparesTime( CurrentTime.getCurrentTime("yyyy-MM-dd"),wheelMain.getTime())){
						if(tv.equals(toYearMonth)){
							if(comparesTime(wheelMain.getTime(), timeSelect.getText().toString())){
								tv.setText(wheelMain.getTime());
								Constant.showLoad(getActivity(), leibie);
								initSortArr();
								
								int size = selectArr.size();
								if(size > 0){
									for (int i = 0; i < 9; i++) {
										if(selectArr.get(0).getsName().equals(Group[i])){
											initHash(i); 
											break;
										}
									}
								}
								for (int i = 1; i < size; i++) {
									getAddHash(hashMap,selectArr.get(i).getsName());
								}
								Constant.dimissLoad();
								Message msg = new Message();
								msg.what = 3;
								msg.obj = hashMap;
								myHandler.sendMessage(msg); 
							}else
								Toast.makeText(getActivity(), "时间不能大于截止时间", 0).show();
						}else{
							tv.setText(wheelMain.getTime());
							Constant.showLoad(getActivity(), leibie);
							initSortArr();
							int size = selectArr.size();
							if(size > 0){
								for (int i = 0; i < 9; i++) {
									if(selectArr.get(0).getsName().equals(Group[i])){
										initHash(i); 
										break;
									}
								}
							}
							for (int i = 1; i < size; i++) {
								getAddHash(hashMap,selectArr.get(i).getsName());
							}
							Constant.dimissLoad();
							Message msg = new Message();
							msg.what = 3;
							msg.obj = hashMap;
							myHandler.sendMessage(msg); 
						}
					}else{
						Toast.makeText(getActivity(), "时间不能大于今天", 0).show();
					}
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	}
	
	private boolean comparesTime(String t1,String t2){
		 long days = 0;
		 long diff = 0;
		try
		{
		    Date d1 = dateFormat.parse(t1);
		    Date d2 = dateFormat.parse(t2);
		    diff = d1.getTime() - d2.getTime();
		    System.out.println("t1 -t2 = "+diff);
//		    days = diff / (1000 * 60 * 60 * 24);
		}
		catch (Exception e)
		{
		}
		 return diff >= 0?true:false;
	}
	
	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(currenPosition != position){
			if (position == 2) {
				currenPosition = 2;
				leibie.setText("客户比例");
			} else if (position == 0) {
				currenPosition = 0;
				leibie.setText("已成交");
			}else if(position == 1){
				currenPosition = 1;
				leibie.setText("未成交");
			}
			myHandler.sendEmptyMessage(6);
			}
			popMenu.dismiss();
			
		}
	};
	public Animation getRotateAnimation(float fromDegrees ,float toDegrees,int durationMillis){
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}
	
	private void initHash(int currentItem){
		hashMap.clear();
		if(sortArr.size() > 0){
			String[] titles = switchTitles(currentItem);
//			int[] valuesList1 = new int[titles.length];
			HashMap<Integer,ArrayList<ConsumerModel>> temp = new HashMap<Integer,ArrayList<ConsumerModel>>();
			int arrLength = titles.length;
			int length = sortArr.size();
			for (int i = 0; i < length; i++) {
				
				switch(currentItem){
				case 0:
					for (int j = 0; j < arrLength; j++) {
						
							if(sortArr.get(i).getYixiang().equals(titles[j])){
//								valuesList1[j]++;
								if(temp.get(j) == null){
									ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
									temp.put(j, arr);
								}
								temp.get(j).add(sortArr.get(i));
								System.out.println(titles[j]+sortArr.get(i).getYixiang());
								break;
							}else {
								System.out.println(titles[j]+"....."+sortArr.get(i).getYixiang());
							}
						
					}
					break;
				case 1:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getYixiangyetai().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
								temp.put(j, arr);
							}
							temp.get(j).add(sortArr.get(i));
							System.out.println(titles[j]+sortArr.get(i).getYixiangyetai());
							break;
						}
					}
					break;
				case 2:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getYixianghuxing().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
								temp.put(j, arr);
							}
							temp.get(j).add(sortArr.get(i));
							System.out.println(titles[j]+sortArr.get(i).getYixianghuxing());
							break;
						}
					}
					break;
				case 3:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getYixiangmianji().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
								temp.put(j, arr);
							}
							temp.get(j).add(sortArr.get(i));
							System.out.println(titles[j]+sortArr.get(i).getYixiangmianji());
							break;
						}
					}
					break;
				case 4:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getRenzhiqudao().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
								temp.put(j, arr);
							}
							temp.get(j).add(sortArr.get(i));
							System.out.println(titles[j]+sortArr.get(i).getRenzhiqudao());
							break;
						}
					}
					break;
				case 5:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getZijinshili().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
								temp.put(j, arr);
							}
							temp.get(j).add(sortArr.get(i));
							System.out.println(titles[j]+sortArr.get(i).getZijinshili());
							break;
						}
					}
					break;
				case 6:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getKehushuxing().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
								temp.put(j, arr);
							}
							temp.get(j).add(sortArr.get(i));
							System.out.println(titles[j]+sortArr.get(i).getKehushuxing());
							break;
						}
					}
					break;
				case 7:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getKehuzu().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
								temp.put(j, arr);
							}
							temp.get(j).add(sortArr.get(i));
							System.out.println(titles[j]+sortArr.get(i).getKehuzu());
							break;
						}
					}
					break;
				case 8:
					for (int j = 0; j < arrLength; j++) {
						if(sortArr.get(i).getYisuan().equals(titles[j])){
//							valuesList1[j]++;
							if(temp.get(j) == null){
								ArrayList<ConsumerModel> arr = new ArrayList<ConsumerModel>();
									temp.put(j, arr);
							}
								temp.get(j).add(sortArr.get(i));
								System.out.println(titles[j]+sortArr.get(i).getYisuan());
							break;
						}
					}
					break;
				}
				
				
			}
			for (int j = 0; j < arrLength; j++) {
					if(titles[j].equals("")){
						if(temp.get(j) != null)
							hashMap.put("未知", temp.get(j));
					}else{
						if(temp.get(j) != null)
							hashMap.put(titles[j], temp.get(j));
					}
			}
			
		}
	}
	
	private void initData() {
		if(pDia == null)
			Constant.showLoad(getActivity(), leibie);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!Constant.isLoading){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			if((selectArr.size() == 1 && selectArr.get(0).isSelect()) || selectArr.size() == 0){
				initHash(currentItem);
				 Message msg = new Message();
				 msg.what = 2;
				 msg.obj = hashMap;
				 myHandler.sendMessage(msg);
			}else{
				hashMap = getAddHash(hashMap,Group[currentItem]);
				 Message msg = new Message();
				 msg.what = 2;
				 msg.obj = hashMap;
				 myHandler.sendMessage(msg);
			}
			}
		}).start();
	}
	
	private HashMap<String, ArrayList<ConsumerModel>> getAddHash(HashMap<String, ArrayList<ConsumerModel>> hash,String select){
		String[] titles = null;
		HashMap<String, ArrayList<ConsumerModel>> temp = new HashMap<String, ArrayList<ConsumerModel>>();
		if(select.equals("意向")){
			titles = getResources().getStringArray(R.array.yixiang);
			Iterator iterator = hash.entrySet().iterator();
			while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getYixiang().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
			
			
		}else if(select.equals("业态")){
			titles = getResources().getStringArray(R.array.yixiangyetai);
			Iterator iterator = hash.entrySet().iterator();
			while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getYixiangyetai().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}else if(select.equals("户型")){
			titles = getResources().getStringArray(R.array.yixianghuxing);
			Iterator iterator = hash.entrySet().iterator();
			while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getYixianghuxing().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}else if(select.equals("面积")){
			titles = getResources().getStringArray(R.array.yixiangmianji);
			 Iterator iterator = hashMap.entrySet().iterator();
			while(iterator.hasNext()) {
			Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getYixiangmianji().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}else if(select.equals("认知渠道")){
			titles = getResources().getStringArray(R.array.renzhiqudao);
			 Iterator iterator = hashMap.entrySet().iterator();
				while(iterator.hasNext()) {
				Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getRenzhiqudao().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}else if(select.equals("资金实力")){
			titles = getResources().getStringArray(R.array.zijinshili);
			 Iterator iterator = hashMap.entrySet().iterator();
				while(iterator.hasNext()) {
				Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getZijinshili().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}else if(select.equals("客户属性")){
			titles = getResources().getStringArray(R.array.kehushuxing);
			 Iterator iterator = hashMap.entrySet().iterator();
				while(iterator.hasNext()) {
				Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getKehushuxing().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}else if(select.equals("客户组")){
			titles = getResources().getStringArray(R.array.kehuzu);
			 Iterator iterator = hashMap.entrySet().iterator();
				while(iterator.hasNext()) {
				Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getKehuzu().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get(entry.getKey()+"-"+titles[j]) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put(entry.getKey()+"-"+titles[j], t);
						}
						temp.get(entry.getKey()+"-"+titles[j]).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}else if(select.equals("预算")){
			titles = getResources().getStringArray(R.array.yusuan);
			 Iterator iterator = hashMap.entrySet().iterator();
				while(iterator.hasNext()) {
				Map.Entry entry = (Map.Entry)iterator.next();
			int size = hash.get(entry.getKey()).size();
			int length = titles.length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < length; j++) {
					if(hash.get(entry.getKey()).get(i).getYisuan().equals(titles[j])){
						if(titles[j].equals("")){
							if(temp.get(entry.getKey()+"-未知") == null){
								ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
								temp.put(entry.getKey()+"-未知", t);
							}
							temp.get(entry.getKey()+"-未知").add(hash.get(entry.getKey()).get(i));
						}else{
						if(temp.get((entry.getKey()+"-"+titles[j])) == null){
							ArrayList<ConsumerModel> t = new ArrayList<ConsumerModel>();
							temp.put((entry.getKey()+"-"+titles[j]), t);
						}
						temp.get((entry.getKey()+"-"+titles[j])).add(hash.get(entry.getKey()).get(i));
						}
				}
					
				}
			}
			}
		}
		hash = temp;
		return hash;
	}
	
	private String[] switchTitles(int count){
		String[] titles = null;
		switch(count){
		case 0:
			titles = getResources().getStringArray(R.array.yixiang);
			break;
		case 1:
			titles = getResources().getStringArray(R.array.yixiangyetai);
			break;
		case 2:
			titles = getResources().getStringArray(R.array.yixianghuxing);
			break;
		case 3:
			titles = getResources().getStringArray(R.array.yixiangmianji);
			break;
		case 4:
			titles = getResources().getStringArray(R.array.renzhiqudao);
			break;
		case 5:
			titles = getResources().getStringArray(R.array.zijinshili);
			break;
		case 6:
			titles = getResources().getStringArray(R.array.kehushuxing);
			break;
		case 7:
			titles = getResources().getStringArray(R.array.kehuzu);
			break;
		case 8:
			titles = getResources().getStringArray(R.array.yusuan);
			break;
		}
		return titles;
	}
	
	 protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
		      List<String[]> titles, List<double[]> values) {
		    MultipleCategorySeries series = new MultipleCategorySeries(title);
		    int k = 0;
		    for (double[] value : values) {
		      series.add(2007 + k + "", titles.get(k), value);
		      k++;
		    }
		    return series;
		  }
	 protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		    DefaultRenderer renderer = new DefaultRenderer();
		    renderer.setLabelsTextSize(15);
		    renderer.setLegendTextSize(15);
		    renderer.setMargins(new int[] { 20, 30, 15, 0 });
		    for (int color : colors) {
		      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		      r.setColor(color);
		      renderer.addSeriesRenderer(r);
		    }
		    return renderer;
		  }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.threefragment, container, false);
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(m_BroadcastReceiver);
		super.onDestroy();
	}
	
	
	
}
