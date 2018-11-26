package com.assistant.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.assistant.adapter.DayAdapter;
import com.assistant.adapter.SmsInfoAdapter;
import com.assistant.model.ConsumerModel;
import com.assistant.model.SmsContent;
import com.assistant.model.SmsInfo;
import com.assistant.model.TodayStatistics;
import com.assistant.service.PhoneReceiver;
import com.assistant.utils.Constant;
import com.assistant.utils.CurrentTime;
import com.assistant.utils.NetworkData;
import com.assistant.view.Home;
import com.example.assistant.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TwoFragment extends Fragment implements OnClickListener{
	private ListView dayList,phoneList;
	private ArrayList<TodayStatistics> toDayList = new ArrayList<TodayStatistics>();
	ArrayList<TodayStatistics> temp = new  ArrayList<TodayStatistics>();
	private	DayAdapter dAdapter;
	private	PopupWindow phonePop;
	private	PhoneAdapter pAdapter;
	private	View popView;
	private	int position = 0;
	private	LayoutInflater mInflater;
	private LinearLayout progress_show;
	private int i = 0;
	private	String content = "";
	private	TextView title,name,quyu,zhanghao,dayXinFang,dayFuFang,dayLaiFang,dayYiXiangQiang,
	dayYiXiangZhong,dayYiXiangYiBan,dayLaiDian,dayDianZhuanFang,dayXinZengRenGou,dayXinZengQianYue,
	monthXinFang,monthFuFang,monthLaiFang,monthYiXiangQiang,
	monthYiXiangZhong,monthYiXiangYiBan,monthLaiDian,monthDianZhuanFang,monthXinZengRenGou,monthXinZengQianYue,
	yearTao,yearMoney;
	
	 public BroadcastReceiver m_BroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("SETDATA")) {
					position = (Integer) intent.getExtras().get("position");
					initDay(temp.get(position));
				}else if(intent.getAction().equals("SETDATAANDMONTH")){
					if(temp.size() > 0){
						dAdapter = new DayAdapter(getActivity(), temp);
						dayList.setAdapter(dAdapter);
						position = (Integer) intent.getExtras().get("position");
						initDay(temp.get(position));
					}
					initDayMonth();
					PhoneReceiver.dimissLoad();
				}else if(intent.getAction().equals("updatecall")){
					initData();
				}else if(intent.getAction().equals("update")){
					initData();
				}
			}

	 };
	 
	 Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				PhoneReceiver.showLoad(getActivity(), yearMoney);
			}else if(msg.what == 2){
				if(temp.size() > 0){
					dAdapter = new DayAdapter(getActivity(), temp);
					dayList.setAdapter(dAdapter);
					position = msg.arg1;
					initDay(temp.get(position));
				}
				initDayMonth();
				PhoneReceiver.dimissLoad();
			}else{
				
			}
		}
		 
	 };
	 
	Comparator<TodayStatistics> comparator = new Comparator<TodayStatistics>(){

			@Override
			public int compare(TodayStatistics con1, TodayStatistics con2) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = null,date2 = null;
				try {
					 date1 = df.parse(con1.getMonth());
					 date2 = df.parse(con2.getMonth());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return (int) (date2.getTime() - date1.getTime());
			}
		};
	 private void register_BroadCast() {
			IntentFilter filter = new IntentFilter();
			filter.addAction("SETDATA");
			filter.addAction("SETDATAANDMONTH");
			filter.addAction("update");
			filter.addAction("updatecall");
			filter.setPriority(Integer.MAX_VALUE);
			getActivity().registerReceiver(m_BroadcastReceiver, filter);
		}
	 
	 private void initDay(TodayStatistics today){
		 
		 	dayXinFang.setText(""+today.getDayXinFang());
			dayFuFang.setText(""+today.getDayFuFang());
//			dayYiXiang.setText(""+today.getDayYiXiang());
			dayLaiFang.setText(""+today.getDayLaiFang());
			dayYiXiangQiang.setText(""+today.getDayYiXiangQiang());
			dayYiXiangYiBan.setText(""+today.getDayYiXiangYiBan());
			dayYiXiangZhong.setText(""+today.getDayYiXiangZhong());
			dayLaiDian.setText(""+today.getDayLaiDian());
			dayDianZhuanFang.setText(""+today.getDayDianZhuanFang());
//			dayXinZengRenGou.setText(""+today.getDayXinZengRenGou());
			
			int XinZengQianYue = 0;
			int size = Constant.conLst.size();
			for (int i = 0; i < size; i++) {
				ConsumerModel model = Constant.conLst.get(i);
				if(!model.getKehuleixing().equals("新客户") && model.getTemp9().substring(0, 10).equals(today.getMonth()) 
						&& !model.getChengjiaojine().equals("")){
					XinZengQianYue++;
			}
		}
			
			
			dayXinZengQianYue.setText(""+XinZengQianYue);
			
	 }
	 
	private void initDayMonth() {
		if(toDayList != null){
		ArrayList<TodayStatistics> month = (ArrayList<TodayStatistics>) deepCopy(toDayList);
		HashMap<String,TodayStatistics> map = new HashMap<String,TodayStatistics>();
		int XinFang = 0,FuFang = 0,YiXiang = 0,LaiFang = 0,YiXiangQiang = 0,YiXiangYiBan = 0,
				YiXiangZhong = 0,LaiDian = 0,DianZhuanFang = 0,yearXinZengRenGou = 0,XinZengRenGou = 0,XinZengQianYue = 0,jine = 0;
		if(month != null){
			int size = month.size();
		for (int i = 0; i < size; i++) {
			TodayStatistics model = month.get(i);
			if(map.get(model.getPhone()) == null){
//				TodayStatistics temp = new TodayStatistics();
//				temp.setDayXinFang(model.getDayXinFang());
//				temp.setDayFuFang(model.getDayFuFang());
//				temp.setDayYiXiang(model.getDayYiXiang());
//				temp.setDayLaiFang(model.getDayLaiFang());
//				temp.setDayYiXiangQiang(model.getDayYiXiangQiang());
//				temp.setDayYiXiangYiBan(model.getDayYiXiangYiBan());
//				temp.setDayYiXiangZhong(model.getDayYiXiangZhong());
//				temp.setDayDianZhuanFang(model.getDayDianZhuanFang());
//				temp.setDayLaiDian(model.getDayLaiDian());
//				temp.setDayXinZengRenGou(model.getDayXinZengRenGou());
//				temp.setDayXinZengQianYue(model.getDayXinZengQianYue());
				map.put(model.getPhone(), model);
			}
			else{
				TodayStatistics myModel = map.get(model.getPhone());
			
				myModel.setDayXinFang(myModel.getDayXinFang() + model.getDayXinFang());
				myModel.setDayFuFang(myModel.getDayFuFang() + model.getDayFuFang());
//				myModel.setDayYiXiang(myModel.getDayYiXiang() + model.getDayYiXiang());
				myModel.setDayLaiFang(myModel.getDayLaiFang() + model.getDayLaiFang());
				myModel.setDayYiXiangQiang(myModel.getDayYiXiangQiang() + model.getDayYiXiangQiang());
				myModel.setDayYiXiangYiBan(myModel.getDayYiXiangYiBan() + model.getDayYiXiangYiBan());
				myModel.setDayYiXiangZhong(myModel.getDayYiXiangZhong() + model.getDayYiXiangZhong());
				myModel.setDayDianZhuanFang(myModel.getDayDianZhuanFang() + model.getDayDianZhuanFang());
				myModel.setDayLaiDian(myModel.getDayLaiDian() + model.getDayLaiDian());
				myModel.setDayXinZengRenGou(myModel.getDayXinZengRenGou() + model.getDayXinZengRenGou());
				myModel.setDayXinZengQianYue(myModel.getDayXinZengQianYue() + model.getDayXinZengQianYue());
				map.put(model.getPhone(), myModel);
			}
		}
		}
		
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry obj = (Entry) it.next();
			TodayStatistics model = map.get(obj.getKey());
			if(model.getDayXinFang() > 0)
				XinFang ++;
			if(model.getDayFuFang() > 0)
				FuFang++;
			if(model.getDayYiXiang() > 0)
				YiXiang++;
			if(model.getDayLaiFang() > 0)
				LaiFang++;
			if(model.getDayYiXiangQiang() > 0)
				YiXiangQiang++;
			if(model.getDayYiXiangYiBan() > 0)
				YiXiangYiBan++;
			if(model.getDayYiXiangZhong() > 0)
				YiXiangZhong++;
			if(model.getDayLaiDian() > 0)
				LaiDian++;
			if(model.getDayDianZhuanFang() > 0)
				DianZhuanFang++;
//			if(model.getDayXinZengRenGou() > 0)
//				XinZengRenGou++;
//			if(model.getDayXinZengQianYue() > 0)
//				XinZengQianYue++;

		}
		
		int size = Constant.conLst.size();
		String month1 = CurrentTime.getCurrentTime("yyyy-MM");
		for (int i = 0; i < size; i++) {
				ConsumerModel model = Constant.conLst.get(i);
				if(!model.getKehushuxing().equals("新客户") && !model.getChengjiaojine().equals("") 
						&& model.getTemp9().substring(0, 7).equals(month1)){
					XinZengQianYue++;
			}
		}
		
		
		monthXinFang.setText(""+XinFang);
		monthFuFang.setText(""+FuFang);
//		monthYiXiang.setText(""+YiXiang);
		monthLaiFang.setText(""+LaiFang);
		monthYiXiangQiang.setText(""+YiXiangQiang);
		monthYiXiangYiBan.setText(""+YiXiangYiBan);
		monthYiXiangZhong.setText(""+YiXiangZhong);
		monthLaiDian.setText(""+LaiDian);
		monthDianZhuanFang.setText(""+DianZhuanFang);
		monthXinZengRenGou.setText(""+XinZengRenGou);
		monthXinZengQianYue.setText(""+XinZengQianYue);
		
		int size1 = Constant.conLst.size();
		for (int i = 0; i < size1; i++) {
			if(!Constant.conLst.get(i).getChengjiaojine().equals("") && !Constant.conLst.get(i).getKehushuxing().equals("新客户")){
				yearXinZengRenGou ++;
				jine += Integer.parseInt(Constant.conLst.get(i).getChengjiaojine());
			}
		}
		
		yearTao.setText(""+yearXinZengRenGou+"套");
		yearMoney.setText(jine+"元");
		}
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initUI();
		register_BroadCast();
		initListener();
		initData();
	}


	private void initListener() {
		
		dayXinFang.setOnClickListener(this);
		dayFuFang.setOnClickListener(this);
//		dayYiXiang.setOnClickListener(this);
		dayLaiFang.setOnClickListener(this);
		dayYiXiangQiang.setOnClickListener(this);
		dayYiXiangZhong.setOnClickListener(this);
		dayYiXiangYiBan.setOnClickListener(this);
		dayLaiDian.setOnClickListener(this);
		dayDianZhuanFang.setOnClickListener(this);
		dayXinZengRenGou.setOnClickListener(this);
		dayXinZengQianYue.setOnClickListener(this);
		monthXinFang.setOnClickListener(this);
		monthFuFang.setOnClickListener(this);
//		monthYiXiang.setOnClickListener(this);
		monthLaiFang.setOnClickListener(this);
		monthYiXiangQiang.setOnClickListener(this);
		monthYiXiangZhong.setOnClickListener(this);
		monthYiXiangYiBan.setOnClickListener(this);
		monthLaiDian.setOnClickListener(this);
		monthXinZengRenGou.setOnClickListener(this);
		monthDianZhuanFang.setOnClickListener(this);
		monthXinZengQianYue.setOnClickListener(this);
		phonePop.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				progress_show.setVisibility(View.VISIBLE);
			}
		});
	}

	public List deepCopy(List src){   
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();   
        ObjectOutputStream out;
        List dest = null;
		try {
			out = new ObjectOutputStream(byteOut);
			 out.writeObject(src);   
		       
		        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());   
		        ObjectInputStream in =new ObjectInputStream(byteIn);   
		        dest = (List)in.readObject(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}   
        return dest;   
    }   
	
	private void initData() {
		
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			if(i == 0)
				i++;
			else
				myHandler.sendEmptyMessage(1);
			String arr = NetworkData.getMyModel(Constant.GETDAYDETAIL+"&myphone="+Constant.USERPHONE);
			if(arr != null && !arr.equals("") && !arr.equals("error")){
				Gson gson = new Gson();
				toDayList.clear();
				toDayList.addAll((Collection<? extends TodayStatistics>) gson.fromJson(arr,
						new TypeToken<ArrayList<TodayStatistics>>() {}.getType()));
				 temp.clear();
				 temp = (ArrayList<TodayStatistics>) deepCopy(toDayList);
				if(temp.size() > 0){
				for (int i = 0; i < temp.size(); i++) {
					for (int j = i+1; j < temp.size(); j++) {
						if(temp.get(i).getMonth().equals(temp.get(j).getMonth())){
								if(!temp.get(j).getPhone().equals(temp.get(i).getPhone())){
									
								temp.get(i).setDayLaiDian(temp.get(i).getDayLaiDian()+temp.get(j).getDayLaiDian());
								
								if(temp.get(j).getDayYiXiangQiang() == 1){
									temp.get(i).setDayYiXiangQiang(temp.get(i).getDayYiXiangQiang()+1);
								}else if(temp.get(j).getDayYiXiangZhong() == 1){
									temp.get(i).setDayYiXiangZhong(temp.get(i).getDayYiXiangZhong()+1);
								}else if(temp.get(j).getDayYiXiangYiBan() == 1){
									temp.get(i).setDayYiXiangYiBan(temp.get(i).getDayYiXiangYiBan()+1);
								}
								
								if(temp.get(j).getDayXinFang() == 1){
									temp.get(i).setDayXinFang(temp.get(i).getDayXinFang()+1);
								}else if(temp.get(j).getDayFuFang() == 1){
									temp.get(i).setDayFuFang(temp.get(i).getDayFuFang()+1);
								}
							
							
								if(temp.get(j).getDayXinZengQianYue() == 1){
									temp.get(i).setDayXinZengQianYue(temp.get(i).getDayXinZengQianYue()+1);
								}
								}
							temp.remove(j);
							j--;
						}
					}
				}
				Collections.sort(temp,comparator);
//				Intent it = new Intent("SETDATAANDMONTH");
//				it.putExtra("position", 0);
//				getActivity().sendBroadcast(it);
				}
		}
			Message msg = new Message();
			msg.what = 2;
			msg.arg1 = position;
			myHandler.sendMessage(msg);
		}
	}).start();
		
	}

	private void initUI() {
		mInflater = LayoutInflater.from(getActivity());
		popView = mInflater.inflate(R.layout.phonepop, null);
		phoneList = (ListView) popView.findViewById(R.id.phonelist);
		title = (TextView) popView.findViewById(R.id.title);
		progress_show = (LinearLayout) popView.findViewById(R.id.progress_show);
		phonePop = new PopupWindow(popView, Constant.scWidth/2, Constant.scHeight*2/3);
		phonePop.setFocusable(true);
		phonePop.setBackgroundDrawable(new BitmapDrawable());
		
		dayList = (ListView) getView().findViewById(R.id.dayList);
		name = (TextView) getView().findViewById(R.id.name);
		quyu = (TextView) getView().findViewById(R.id.quyu);
		zhanghao = (TextView) getView().findViewById(R.id.zhanghao);
		dayXinFang = (TextView) getView().findViewById(R.id.dayXinFang);
		dayFuFang = (TextView) getView().findViewById(R.id.dayFuFang);
//		dayYiXiang = (TextView) getView().findViewById(R.id.dayYiXiang);
		dayLaiFang = (TextView) getView().findViewById(R.id.dayLaiFang);
		dayYiXiangQiang = (TextView) getView().findViewById(R.id.dayYiXiangQiang);
		dayYiXiangZhong = (TextView) getView().findViewById(R.id.dayYiXiangZhong);
		dayYiXiangYiBan = (TextView) getView().findViewById(R.id.dayYiXiangYiBan);
		dayLaiDian = (TextView) getView().findViewById(R.id.dayLaiDian);
		dayDianZhuanFang = (TextView) getView().findViewById(R.id.dayDianZhuanFang);
		dayXinZengRenGou = (TextView) getView().findViewById(R.id.dayXinZengRenGou);
		dayXinZengQianYue = (TextView) getView().findViewById(R.id.dayXinZengQianYue);
		monthXinFang = (TextView) getView().findViewById(R.id.monthXinFang);
		monthFuFang = (TextView) getView().findViewById(R.id.monthFuFang);
//		monthYiXiang = (TextView) getView().findViewById(R.id.monthYiXiang);
		monthLaiFang = (TextView) getView().findViewById(R.id.monthLaiFang);
		monthYiXiangQiang = (TextView) getView().findViewById(R.id.monthYiXiangQiang);
		monthYiXiangZhong = (TextView) getView().findViewById(R.id.monthYiXiangZhong);
		monthYiXiangYiBan = (TextView) getView().findViewById(R.id.monthYiXiangYiBan);
		monthLaiDian = (TextView) getView().findViewById(R.id.monthLaiDian);
		monthDianZhuanFang = (TextView) getView().findViewById(R.id.monthDianZhuanFang);
		monthXinZengRenGou = (TextView) getView().findViewById(R.id.monthXinZengRenGou);
		monthXinZengQianYue = (TextView) getView().findViewById(R.id.monthXinZengQianYue);
		yearTao = (TextView) getView().findViewById(R.id.yearTao);
		yearMoney = (TextView) getView().findViewById(R.id.yearMoney);
		
		name.setText(Constant.USERNAME);
		quyu.setText(Constant.QUYU);
		zhanghao.setText(Constant.USERPHONE);
	}

	private void showPop(){
		 int[] location = new int[2];
		 title.getLocationOnScreen(location); 
		 progress_show.setVisibility(View.GONE);
		 phonePop.setAnimationStyle(R.style.PopupAnimation);
		 phonePop.showAtLocation(title, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
	}
	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(m_BroadcastReceiver);
		super.onDestroy();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.twofragment, container, false);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.dayXinFang:
			if(!dayXinFang.getText().toString().equals("0")){
				title.setText("今日新仿");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayXinFang() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayFuFang:
			if(!dayFuFang.getText().toString().equals("0")){
				title.setText("今日复仿");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayFuFang() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
//		case R.id.dayYiXiang:
//			if(!dayLaiFang.getText().toString().equals("0")){
//				title.setText("意向客户");
//				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
//				int size = toDayList.size();
//				for (int i = 0; i < size; i++) {
//					TodayStatistics today = toDayList.get(i);
//					if(today.getDayYiXiang() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
//						temp1.add(today);
//					}
//				}
//				pAdapter = new PhoneAdapter(temp1);
//				phoneList.setAdapter(pAdapter);
//				showPop();
//			}
//			break;
		case R.id.dayLaiFang:
			if(!dayLaiFang.getText().toString().equals("0")){
				title.setText("今日来访");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayLaiFang() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayYiXiangQiang:
			if(!dayYiXiangQiang.getText().toString().equals("0")){
				title.setText("意向强");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayYiXiangQiang() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayYiXiangZhong:
			if(!dayYiXiangZhong.getText().toString().equals("0")){
				title.setText("意向中");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayYiXiangZhong() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayYiXiangYiBan:
			if(!dayYiXiangYiBan.getText().toString().equals("0")){
				title.setText("意向一般");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayYiXiangYiBan() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayLaiDian:
			if(!dayLaiDian.getText().toString().equals("0")){
				title.setText("来电客户");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayLaiDian() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayDianZhuanFang:
			if(!dayDianZhuanFang.getText().toString().equals("0")){
				title.setText("电转访");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayDianZhuanFang() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayXinZengRenGou:
			if(!dayXinZengRenGou.getText().toString().equals("0")){
				title.setText("新增认购");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayXinZengRenGou() > 0 && today.getMonth().equals(temp.get(position).getMonth())){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.dayXinZengQianYue:
			if(!dayXinZengQianYue.getText().toString().equals("0")){
				title.setText("新增签约");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = Constant.conLst.size();
				String month = temp.get(position).getMonth();
				for (int i = 0; i < size; i++) {
					ConsumerModel model = Constant.conLst.get(i);
					if(!model.getKehuleixing().equals("新客户") && model.getTemp9().substring(0, 10).equals(month)){
						TodayStatistics today = new TodayStatistics();
						today.setName(model.getCustomer_name());
						today.setPhone(model.getCustomer_phone());
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(temp1);
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthXinFang:
			if(!monthXinFang.getText().toString().equals("0")){
				title.setText("今日新仿");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayXinFang() > 0){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthFuFang:
			if(!monthFuFang.getText().toString().equals("0")){
				title.setText("今日复仿");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayFuFang() > 0){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
//		case R.id.monthYiXiang:
//			if(!monthYiXiang.getText().toString().equals("0")){
//				title.setText("意向客户");
//				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
//				int size = toDayList.size();
//				for (int i = 0; i < size; i++) {
//					TodayStatistics today = toDayList.get(i);
//					if(today.getDayYiXiang() > 0){
//						temp1.add(today);
//					}
//				}
//				pAdapter = new PhoneAdapter(removeMuti(temp1));
//				phoneList.setAdapter(pAdapter);
//				showPop();
//			}
//			break;
		case R.id.monthLaiFang:
			if(!monthLaiFang.getText().toString().equals("0")){
				title.setText("月来仿");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayLaiFang() > 0){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthYiXiangQiang:
			if(!monthYiXiangQiang.getText().toString().equals("0")){
				title.setText("意向强");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayYiXiangQiang() > 0){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthYiXiangZhong:
			if(!monthYiXiangZhong.getText().toString().equals("0")){
				title.setText("意向中");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayYiXiangZhong() > 0){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthYiXiangYiBan:
			if(!monthYiXiangYiBan.getText().toString().equals("0")){
				title.setText("意向一般");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayYiXiangYiBan() > 0){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthLaiDian:
			if(!monthLaiDian.getText().toString().equals("0")){
				title.setText("来电客户");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayLaiDian() > 0){
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthXinZengRenGou:
			if(!monthXinZengRenGou.getText().toString().equals("0")){
				title.setText("新增认购");
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				int size = toDayList.size();
				for (int i = 0; i < size; i++) {
					TodayStatistics today = toDayList.get(i);
					if(today.getDayXinZengRenGou() > 0){
						temp1.add(today);
					}
				}
				
				for (int i = 0; i < temp1.size(); i++) {
					for (int j = i+1; j < temp1.size(); j++) {
						if(temp1.get(i).getPhone().equals(temp1.get(j).getPhone())){
							temp1.remove(j);
							j --;
						}
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
			}
			break;
		case R.id.monthXinZengQianYue:
			if(!monthXinZengQianYue.getText().toString().equals("0")){
				ArrayList<TodayStatistics> temp1 = new ArrayList<TodayStatistics>();
				String month = temp.get(position).getMonth().substring(0, 7);
				title.setText("新增签约");
				int size = Constant.conLst.size();
				for (int i = 0; i < size; i++) {
					ConsumerModel model = Constant.conLst.get(i);
					if(!model.getKehushuxing().equals("新客户") && !model.getChengjiaojine().equals("")
							 && model.getTemp9().substring(0, 7).equals(month)){
						TodayStatistics today = new TodayStatistics();
						today.setName(model.getCustomer_name());
						today.setPhone(model.getCustomer_phone());
						temp1.add(today);
					}
				}
				pAdapter = new PhoneAdapter(removeMuti(temp1));
				phoneList.setAdapter(pAdapter);
				showPop();
				
			}
			break;
		}
	}
	
	private ArrayList<TodayStatistics> removeMuti( ArrayList<TodayStatistics> temp1){
		
		for (int i = 0; i < temp1.size(); i++) {
			for (int j = i+1; j < temp1.size(); j++) {
				if(temp1.get(i).getPhone().equals(temp1.get(j).getPhone())){
					temp1.remove(j);
					j --;
				}
			}
		}
		
		
		return temp1;
		
	}
	
	class PhoneAdapter extends BaseAdapter{
		ArrayList<TodayStatistics> arrList;
		PopupWindow sendMsgPop;
		public PhoneAdapter(ArrayList<TodayStatistics> arrList){
			this.arrList = arrList;
		}
		@Override
		public int getCount() {
			return arrList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TodayStatistics today = arrList.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.phone_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.call = (ImageButton) convertView.findViewById(R.id.call);
				holder.message = (ImageButton) convertView.findViewById(R.id.message);
				convertView.setTag(holder);
			}else
				holder = (ViewHolder) convertView.getTag();
			holder.name.setText(today.getName());
			holder.phone.setText(today.getPhone());
			holder.call.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!today.getPhone().equals("") && today.getPhone().length() == 11){
						if(Constant.isCanUseSim(getActivity())){
						Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
			            .parse("tel:" + today.getPhone()));  
				        startActivity(dialIntent); 
						}
					}else{
						Toast.makeText(getActivity(), "号码不正确", 0).show();
					}
				}
			});
			holder.message.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!today.getPhone().equals("") && today.getPhone().length() == 11){
						if(Constant.isCanUseSim(getActivity()))
						createSendMsgPop(today.getPhone(),today.getName());
					}else{
						Toast.makeText(getActivity(), "号码不正确", 0).show();
					}
				}
			});
			return convertView;
		}
		private void createSendMsgPop(final String phone,final String name){
			 if(phone == null){
				 Toast.makeText(getActivity(), "号码为空", 0).show();
			 }else{
			 View popwindow = mInflater.inflate(R.layout.chat_message, null);
			 final EditText edtContent = (EditText) popwindow.findViewById(R.id.edtSend);
			 Button send = (Button) popwindow.findViewById(R.id.sendMsg);
			 ListView infoList = (ListView) popwindow.findViewById(R.id.msgList);
			 edtContent.setText(content);
			 
			 SmsContent sms = new SmsContent((getActivity()), Uri.parse("content://sms/"));
			 final ArrayList<SmsInfo> smsInfos = (ArrayList<SmsInfo>) sms.getOnePersonInfo(phone,name);
			 
			 final SmsInfoAdapter sAdapter = new SmsInfoAdapter(getActivity(), smsInfos);
			 infoList.setAdapter(sAdapter);
			 infoList.setSelection(smsInfos.size());
			 
			 sendMsgPop = new PopupWindow(popwindow, Constant.scWidth/2, Constant.scHeight*3/4);
			 send.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String smsContent = edtContent.getText().toString();
					if(!smsContent.equals("")){
					SmsManager smsManager = SmsManager.getDefault(); 
					if(smsContent.length()>70){  
			             List<String> contents = smsManager.divideMessage(smsContent);  
			             for(String c:contents){  
			                 smsManager.sendTextMessage(phone, null, c, null, null);  
			             }  
			         }else{  
			             smsManager.sendTextMessage(phone, null, smsContent, null, null);  
			         } 
					edtContent.setText("");
					SmsInfo sms = new SmsInfo();
					sms.setDate(CurrentTime.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					sms.setName(name);
					sms.setPhoneNumber(phone);
					sms.setType("0");
					sms.setSmsbody(smsContent);
					smsInfos.add(sms);
					sAdapter.notifyDataSetChanged();
					phoneList.setSelector(smsInfos.size());
				}
				}
			});
			 
			 sendMsgPop.setFocusable(true);
			 sendMsgPop.setBackgroundDrawable(new BitmapDrawable());
			 int[] location = new int[2];
			 send.getLocationOnScreen(location); 
			 sendMsgPop.setAnimationStyle(R.style.PopupAnimation);
			 sendMsgPop.showAtLocation(send, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
			 sendMsgPop.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						content =  edtContent.getText().toString();
					}
				});
		 }
		 }
		class ViewHolder{
			TextView name,phone;
			ImageButton call,message;
		}
		
	}
}
