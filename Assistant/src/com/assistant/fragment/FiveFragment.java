package com.assistant.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.R.color;
import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;

import com.assistant.adapter.GroupAdapter;
import com.assistant.utils.Constant;
import com.assistant.view.PieChartView;
import com.assistant.view.PopMenu;
import com.assistant.view.Register;
import com.example.assistant.R;

public class FiveFragment extends Fragment{
	private LinearLayout rootView;
	private PieChartView mChartView;
	private AbstractWheel select;
	private TextView leibie;
	private boolean scrolling = false;
	private int currentItem = 2;
	private ProgressDialog pDia = null;
	ArrayList<double[]> values;
	int currenPosition = 0;
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
					pDia.cancel();
				}else if(msg.what == 2){
					rootView.removeAllViews();
					mChartView = new PieChartView(getActivity(),(HashMap<String, Integer>) msg.obj);
					rootView.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,
					          LayoutParams.FILL_PARENT));
					pDia.cancel();
				}else{
					showDialog();
				}
			}
			
		};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
		initData();
		initLstener();
	}
	
	private void showDialog(){
		pDia = new ProgressDialog(getActivity());
		pDia.setMessage("正在努力更新 请稍后。。。。");
		pDia.setCancelable(false);
		pDia.setCanceledOnTouchOutside(false);
		pDia.show();
	}
	private void initLstener() {
		leibie.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popMenu.showAsDropDown(v);
			}
		});
		select.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
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
					initData();
				}
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
				initData();
			}
		});
	}
	private void initUI() {
		rootView = (LinearLayout) getActivity().findViewById(R.id.rootView);
		select = (AbstractWheel) getActivity().findViewById(R.id.select);
		leibie = (TextView) getActivity().findViewById(R.id.leibie);
		leibie.setText("成交量");
		
		select.setCyclic(false);
		select.setVisibleItems(5);
		select.setViewAdapter(new GroupAdapter(getActivity(),Group));
		select.setCurrentItem(currentItem);
		
	}
	
	
	
	@Override
	public void onStart() {
		super.onStart();
		popMenu = new PopMenu(getActivity(),getResources().getDimensionPixelSize(R.dimen.popmenu));
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		popMenu.addItems(new String[] { "成交量","客户比例"});
	}



	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(currenPosition != position)
			if (position == 1) {
				currenPosition = 1;
				leibie.setText("客户比例");
			} else if (position == 0) {
				currenPosition = 0;
				leibie.setText("成交量");
			}

			popMenu.dismiss();
		}
	};
	private void initData() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(Constant.tellPhones.length == 0){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				myHandler.sendEmptyMessage(3);
				HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
				String[] titles = null;
				switch(currentItem){
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
					default:
						titles = getResources().getStringArray(R.array.yixianghuxing);
						break;
				}
				int[] valuesList1 = new int[titles.length];
				int arrLength = titles.length;
				int length = Constant.conLst.size();
				for (int i = 0; i < length; i++) {
					
					switch(currentItem){
					case 0:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getYixiang().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 1:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getYixiangyetai().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 2:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getYixianghuxing().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 3:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getYixiangmianji().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 4:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getRenzhiqudao().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 5:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getZijinshili().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 6:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getKehushuxing().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 7:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getKehuzu().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					case 8:
						for (int j = 0; j < arrLength; j++) {
							if(Constant.conLst.get(i).getYisuan().equals(titles[j])){
								valuesList1[j]++;
								break;
							}
						}
						break;
					}
					
					
				}
				for (int j = 0; j < arrLength; j++) {
					System.out.println("j = "+valuesList1[j]);
						hashMap.put(titles[j], valuesList1[j]);
				}
				
				Message msg = new Message();
				msg.what = 2;
				msg.obj = hashMap;
				myHandler.sendMessage(msg);
			}
		}).start();
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
		return inflater.inflate(R.layout.fivefragment, container, false);
	}
	
}
